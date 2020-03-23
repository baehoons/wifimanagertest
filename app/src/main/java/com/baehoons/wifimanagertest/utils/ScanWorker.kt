package com.baehoons.wifimanagertest.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.baehoons.wifimanagertest.viewmodel.CheckmentViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ScanWorker (context: Context, workerParams:WorkerParameters) : Worker(context, workerParams){

    private val TAG = "ScanWorker"
    private var resultedList = ArrayList<ScanResult>()
    private var hh:Boolean = false
    private lateinit var checkmentViewModel: CheckmentViewModel
    private val wifiManager: WifiManager
        get() = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private var resultList = ArrayList<ScanResult>()

    private val wifiReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            applicationContext!!.unregisterReceiver(this)
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
        this.registerReceiver(wifiReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
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

    override fun doWork(): Result {
        if (!wifiManager.isWifiEnabled) {
            //Toast.makeText(this, "와이파이가 연결되어 있지 않네요, 연결합니다.", Toast.LENGTH_LONG).show()
            Log.d(TAG, "와이파이 연결중")
            wifiManager.isWifiEnabled = true

            for (i in 0..9) {
                Log.d(TAG, "run: $i")
                scanWifi()
                update()
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

        }

        return Result.success()
    }

}