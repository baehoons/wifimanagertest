package com.baehoons.wifimanagertest.adapter

import android.app.AlertDialog
import android.content.Context
import android.net.wifi.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baehoons.wifimanagertest.R
import com.baehoons.wifimanagertest.data.Component
import kotlinx.android.synthetic.main.item_wifi_saved.view.*
import com.baehoons.wifimanagertest.view.main.ControlFragment.Mode
import com.baehoons.wifimanagertest.view.main.ControlFragment.Mode.selectedComponent
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.item_wifi_saved.view.signature
import kotlinx.android.synthetic.main.item_wifi_saved.view.ssid_name
import kotlinx.android.synthetic.main.item_wifi_scan.view.*

class WifiSavedListAdapter(val context: Context, val components:List<Component>):RecyclerView.Adapter<WifiSavedListAdapter.DeviceHolder>(){

    var onDeviceClickListener: ((Component) -> Unit)? = null

    private var devices = ArrayList<Component>()
    class DeviceHolder(parent: ViewGroup):RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.item_wifi_saved,parent,false)){
        fun onBind(scanResult: Component, onDeviceClickListener: (Component)->Unit) {
            itemView.run {

                ssid_name.text = scanResult.ssid_w
                signature.text = "BSSID : "+scanResult.bssid_w

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

    fun addDevice(device: Component) {

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

