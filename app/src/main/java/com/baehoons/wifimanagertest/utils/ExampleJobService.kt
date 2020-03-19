package com.baehoons.wifimanagertest.utils

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.baehoons.wifimanagertest.viewmodel.CheckmentViewModel


class ExampleJobService : JobService() {
    val ACTION_DOWLOAD_FINISHED = "actionDownloadFinished"
    private val TAG = "ExampleJobService"
    private var jobCancelled = false
    private var resultedList = ArrayList<ScanResult>()
    private lateinit var checkmentViewModel: CheckmentViewModel
    private val wifiManager: WifiManager
        get() = applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private var resultList = ArrayList<ScanResult>()

    private val wifiReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            unregisterReceiver(this)
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

    private fun scanloop(v: Int) {

        do {
            var tt: Int = 1
            var st = tt * v
            scanWifi()
            Log.d(TAG, "LOADING")
        } while (st < 0)

    }


    private fun scanWifi() {
        Log.d(TAG, "와이파이 초반")
        registerReceiver(wifiReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
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

    private fun scanFailure() {}

    override fun onStartJob(params: JobParameters): Boolean {
        Log.d(TAG, "Job started")
        doBackgroundWork(params)
        return true
    }


    private fun doBackgroundWork(params: JobParameters) {
        val context = this.applicationContext
        checkmentViewModel = ViewModelProviders.of(context as FragmentActivity).get(CheckmentViewModel::class.java)
        Thread(Runnable {

            if (!wifiManager.isWifiEnabled) {
                //Toast.makeText(this, "와이파이가 연결되어 있지 않네요, 연결합니다.", Toast.LENGTH_LONG).show()
                Log.d(TAG, "와이파이 연결중")
                wifiManager.isWifiEnabled = true
            }
            for (i in 0..9) {
                Log.d(TAG, "run: $i")
                scanWifi()
                update()
                if (jobCancelled) {
                    return@Runnable
                }
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            checkmentViewModel.getselect().observeForever()
            for(result in resultedList){

            }


            Log.d(TAG, "Job finished")
            jobFinished(params, false)
        }).start()
    }


    override fun onStopJob(params: JobParameters): Boolean {

        unregisterReceiver(wifiReceiver)
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true
        return true
    }


}