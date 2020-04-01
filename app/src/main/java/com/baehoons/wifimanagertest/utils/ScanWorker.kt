package com.baehoons.wifimanagertest.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.baehoons.wifimanagertest.R
import com.baehoons.wifimanagertest.view.MainActivity
import com.baehoons.wifimanagertest.viewmodel.CheckmentViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ScanWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private val TAG = "ScanWorker"
    private var resultedList = ArrayList<ScanResult>()
    private var hh: Boolean = false
    private var hh_s: Boolean = false
    private var hh_e: Boolean = true
    private lateinit var checkmentViewModel: CheckmentViewModel
    private val wifiManager: WifiManager
        get() = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private val ss = inputData.getString("ssid")
    private val noti = inputData.getString("noti")
    private val noti_e = inputData.getString("noti_e")
    private var resultList = ArrayList<ScanResult>()

    private val wifiReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            applicationContext.unregisterReceiver(this)
            Log.d(TAG, "unregisterReceiver")

            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            Log.d(TAG, "intent.getBooleanExtra")
            if (success) {
                scanSuccess()
                Log.d(TAG, "scan successful")
            } else {
                scanFailure()
                Log.d(TAG, "scan failed")
            }

        }
    }

    private fun update() {
        for (result in resultList) {
            if (!resultedList.contains(result)) {
                resultedList.add(result)
            }
        }
    }


    private fun scanWifi() {
        Log.d(TAG, "와이파이 초반")
        applicationContext.registerReceiver(
            wifiReceiver,
            IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        )
        Log.d(TAG, "와이파이 중간")
        wifiManager.startScan()
        Log.d(TAG, "와이파이 완료")

        //Toast.makeText(this, "Scanning WiFi ...", Toast.LENGTH_SHORT).show()
    }

    private fun scanSuccess() {
        resultList.clear()
        resultList = wifiManager.scanResults as ArrayList<ScanResult>


        for (result in resultList) {

            Log.d(TAG, "SSID: ${result.SSID}")
            Log.d(TAG, "BSSID: ${result.BSSID}")
            Log.d(TAG, "frequency: ${result.frequency} hHz")
            Log.d(TAG, "capabilities: ${result.capabilities}")

            if (ss != null) {
                if (result.SSID == ss) {
                    if (hh == false) {
                        Log.d(TAG, "Connected!!!!!!!!!!!")
                        hh = true
                    }

                    Log.d("ssss", result.SSID)
                }
            }
        }
    }

    private fun scanFailure() {

    }

    fun timenow(): String {
        val realformat: String = "yyyyMMddHHmm"
        var sdfs = SimpleDateFormat(realformat, Locale.KOREA)
        val timenowz = sdfs.format(Date())
        return timenowz
    }

    private fun createOutputData_e(): Data {
        return Data.Builder()
            .putBoolean("end", hh)
            .build()
    }

    private fun createOutputData_s(): Data {
        return Data.Builder()
            .putBoolean("start", hh_s)
            .putBoolean("end",hh)
            .build()
    }

    private fun sendNotification(id:Int){
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, id)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val bitmap = applicationContext.vectorToBitmap(R.drawable.ic_camera_front_black_24dp)
        val titleNotification = "출퇴근 관리"
        val subtitleNotification = "출근 하시겠습니까?"
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val pendingIntent_c = NotificationActivity.getDismissIntent(id, applicationContext)
        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setLargeIcon(bitmap).setSmallIcon(R.drawable.ic_camera_front_black_24dp)
            .setContentTitle(titleNotification).setContentText(subtitleNotification)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            //.addAction(R.drawable.ic_check_circle_black_24dp,"확인",pendingIntent)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_close_black_24dp,"취소",pendingIntent_c)
        notification.priority = NotificationCompat.PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)

            val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH)

            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
            //notificationManager.cancel(intent.getIntExtra(NOTIFICATION_ID,-1))
        }
        notificationManager.notify(id,notification.build())
    }

    companion object {
        const val NOTIFICATION_ID = "appName_notification_id"
        const val NOTIFICATION_NAME = "appName"
        const val NOTIFICATION_CHANNEL = "appName_channel_01"
        const val NOTIFICATION_WORK = "appName_notification_work"
    }

    override fun doWork(): Result {
        if (!wifiManager.isWifiEnabled) {
            //Toast.makeText(this, "와이파이가 연결되어 있지 않네요, 연결합니다.", Toast.LENGTH_LONG).show()
            Log.d(TAG, "와이파이 연결중")
            wifiManager.isWifiEnabled = true
        }

        scanWifi()
        update()
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        scanSuccess()


        val id = inputData.getLong(NOTIFICATION_ID, 0).toInt()

        //Log.d("ssss", hh.toString())
        Log.d("hhhh", hh.toString())
        if(noti == null){
            Log.d("ssss",hh_s.toString())
            if(hh_s==false){
                sendNotification(id)
            }

            hh_s = true
            return Result.success(createOutputData_s())
        }

        else if(noti != null){
            if(noti_e == null){
                if(hh==false){
                    sendNotification(id)
                }

            }
            return Result.success(createOutputData_s())
        }
        else{

            return Result.success(createOutputData_s())
        }

    }

}