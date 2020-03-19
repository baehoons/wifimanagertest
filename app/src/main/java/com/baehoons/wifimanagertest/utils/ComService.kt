package com.baehoons.wifimanagertest.utils

import android.Manifest
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable

class ComService : Service() {
    private val wifiManager: WifiManager
        get() =applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private var resultList = ArrayList<ScanResult>()
    private val TAG = "WIFI_TAG"

    private val wifiReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            unregisterReceiver(this)
            Log.d(TAG, "unregisterReceiver")

            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            Log.d(TAG, "intent.getBooleanExtra")
            if (success) {
                scanSuccess()
                Log.d(TAG, "scan successful")
            }
            else {
                scanFailure()
                Log.d(TAG, "scan failed")
            }

        }


    }


    private fun scanloop(v:Int){

        do{
            var tt:Int = 1
            var st = tt*v
            scanWifi()
            Log.d(TAG,"LOADING")
        } while (st<0)

    }


    private fun scanWifi() {
        registerReceiver(wifiReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        wifiManager.startScan()
        Toast.makeText(this, "Scanning WiFi ...", Toast.LENGTH_SHORT).show()
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

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        if (!wifiManager.isWifiEnabled) {
            Toast.makeText(this, "와이파이가 연결되어 있지 않네요, 연결합니다.", Toast.LENGTH_LONG).show()
            wifiManager.isWifiEnabled = true
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        scanloop(1)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        scanloop(-1)
        super.onDestroy()
    }

}