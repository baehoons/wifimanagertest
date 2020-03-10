package com.baehoons.wifimanagertest.view.main

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.baehoons.wifimanagertest.R
import com.baehoons.wifimanagertest.adapter.WifiScanListAdapter
import com.baehoons.wifimanagertest.utils.ManagePermissions
import kotlinx.android.synthetic.main.fragment_wifi_list.*


class WifiListFragment : Fragment() {
    var navController: NavController?=null
    private lateinit var managePermissions: ManagePermissions
    private val requestCode  = 123
    private val TAG = "WIFI_TAG"
    private val wifiManager: WifiManager
        get() = activity?.applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager

    private var resultList = ArrayList<ScanResult>()

    private var deviceListAdapter:WifiScanListAdapter = WifiScanListAdapter().apply {
        onDeviceClickListener = {onDeviceClicked(it)}
    }

    private val wifiReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            activity?.unregisterReceiver(this)
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

    fun onDeviceClicked(device: ScanResult){
        val ssid = device.SSID
        val bssid = device.BSSID
        val level = calculateSignalLevel(device.level)
        val frequency = (device.frequency).toString()
        val capabilities = device.capabilities
        val bundle = bundleOf(
            "ssid" to ssid,
            "bssid" to bssid,
            "level" to level,
            "frequency" to frequency,
            "capabilities" to capabilities
        )
        navController!!.navigate(R.id.action_wifiListFragment_to_detailFragment,bundle)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wifi_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initWifi()
        scan_text.visibility = View.VISIBLE
        button_scan.setOnClickListener {
            deviceListAdapter.clearDevices()
            managePermissions.checkPermissions()
            if (!wifiManager.isWifiEnabled) {
                Toast.makeText(activity, "와이파이가 연결되어 있지 않네요, 연결합니다.", Toast.LENGTH_LONG).show()
                wifiManager.isWifiEnabled = true
            }
            scan_text.visibility = View.INVISIBLE
            scanWifi()
        }
    }

    private fun initWifi(){
        val list = listOf <String> (
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        managePermissions = ManagePermissions(activity!!, list, requestCode)
    }

    private fun scanWifi() {
        activity?.registerReceiver(wifiReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        wifiManager.startScan()
        Toast.makeText(activity, "Scanning WiFi ...", Toast.LENGTH_SHORT).show()
    }

    private fun scanSuccess() {
        resultList.clear()
        resultList = wifiManager.scanResults as ArrayList<ScanResult>

        updateview()
        for (result in resultList) {

            Log.d(TAG, "SSID: ${result.SSID}")
            Log.d(TAG, "BSSID: ${result.BSSID}")
            Log.d(TAG, "level: ${calculateSignalLevel(result.level)}")
            Log.d(TAG, "frequency: ${result.frequency} hHz")
            Log.d(TAG, "capabilities: ${result.capabilities}")
        }
    }

    private fun updateview(){
        for(result in resultList){
            Log.d("hhoon",result.toString())
            deviceListAdapter.addDevice(result)
        }


        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        recycler_view.adapter = deviceListAdapter
        deviceListAdapter.notifyDataSetChanged()
    }

    private fun scanFailure() {}


    private fun calculateSignalLevel(level: Int) = when {
        level > -50 -> "Excellent"
        level in -60..-50 -> "Good"
        level in -70..-60 -> "Fair"
        level < -70 -> "Weak"
        else -> "No signal"
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        when(requestCode) {
            this.requestCode -> {
                val isPermissionsGranted = managePermissions
                    .processPermissionsResult(requestCode, permissions, grantResults)

                if(isPermissionsGranted){
                    Toast.makeText(activity, "Permissions granted.", Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(activity, "Permissions denied.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
