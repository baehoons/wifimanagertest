package com.baehoons.wifimanagertest.adapter

import android.net.wifi.ScanResult
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baehoons.wifimanagertest.R
import kotlinx.android.synthetic.main.item_wifi_scan.view.*

class WifiScanListAdapter : RecyclerView.Adapter<WifiScanListAdapter.DeviceHolder>(){

    var onDeviceClickListener: ((ScanResult) -> Unit)? = null

    private var devices = ArrayList<ScanResult>()
    class DeviceHolder(parent: ViewGroup):RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.item_wifi_scan,parent,false)){
        fun onBind(scanResult: ScanResult,onDeviceClickListener: (ScanResult)->Unit) {
            itemView.run {
                fun calculateSignalLevel(level: Int) = when {
                    level > -50 -> "Excellent"
                    level in -60..-50 -> "Good"
                    level in -70..-60 -> "Fair"
                    level < -70 -> "Weak"
                    else -> "No signal"
                }
                var levels:String = calculateSignalLevel(scanResult.level)
                var ssid: String = scanResult.SSID
                ssid_name.text = ssid
                signature.text = levels

                scan_item.setOnClickListener {
                    onDeviceClickListener.invoke(scanResult)
                }
            }
        }
    }

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int) = DeviceHolder (
        container
    )

    override fun getItemCount() = devices.size

    override fun onBindViewHolder(viewHolder: DeviceHolder, position: Int) {
        viewHolder.onBind(devices[position]){
            onDeviceClickListener?.invoke(it)
        }

    }

    fun addDevice(device: ScanResult) {

        if(!devices.contains(device)){
            devices.add(device)
            notifyDataSetChanged()
        }

    }

    fun clearDevices() {
        devices.clear()
        notifyDataSetChanged()
    }

}