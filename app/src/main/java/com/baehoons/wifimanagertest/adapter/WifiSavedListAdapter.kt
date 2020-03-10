package com.baehoons.wifimanagertest.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.net.wifi.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.baehoons.wifimanagertest.R
import com.baehoons.wifimanagertest.data.Component
import kotlinx.android.synthetic.main.item_wifi_saved.view.*
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.item_wifi_saved.view.signature
import kotlinx.android.synthetic.main.item_wifi_saved.view.ssid_name
import kotlinx.android.synthetic.main.item_wifi_scan.view.*

class WifiSavedListAdapter:RecyclerView.Adapter<WifiSavedListAdapter.DeviceHolder>(){

    var onDeviceClickListener: ((Component) -> Unit)? = null

    private var devices = ArrayList<Component>()
    class DeviceHolder(parent: ViewGroup):RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.item_wifi_saved,parent,false)){
        fun onBind(scanResult: Component, onDeviceClickListener: (Component)->Unit) {
            itemView.run {

                ssid_name.text = scanResult.ssid_w
                signature.text = "BSSID : "+scanResult.bssid_w
                if(scanResult.selected==true){
                    image_saved.setImageDrawable(resources.getDrawable(R.drawable.ic_wb_incandescent_active_24dp))
                }
                else{
                    image_saved.setImageDrawable(resources.getDrawable(R.drawable.ic_wb_incandescent_black_24dp))
                }

                saved_wifi.setOnClickListener {
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

    fun addDevice(device: ArrayList<Component>) {

        if(!devices.containsAll(device)){
            devices.addAll(device)
            notifyDataSetChanged()
        }

    }

    fun clearDevices() {
        devices.clear()
        notifyDataSetChanged()
    }

}

