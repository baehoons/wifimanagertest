package com.baehoons.wifimanagertest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.baehoons.wifimanagertest.R
import com.baehoons.wifimanagertest.data.Component
import com.baehoons.wifimanagertest.viewmodel.CheckmentViewModel
import com.baehoons.wifimanagertest.viewmodel.ComponentViewModel
import kotlinx.android.synthetic.main.item_wifi_saved.view.*


class WifiSavedListAdapter:RecyclerView.Adapter<WifiSavedListAdapter.DeviceHolder>(){

    var onDeviceClickListener: ((Component) -> Unit)? = null

    private var devices = ArrayList<Component>()
    class DeviceHolder(parent: ViewGroup):RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.item_wifi_saved,parent,false)){
        fun onBind(scanResult: Component, onDeviceClickListener: (Component)->Unit) {
            itemView.run {

                ssid_name.text = scanResult.ssid_w
                signature.text = "BSSID : "+scanResult.bssid_w
//                val context:Context = this.context
//
//                var checkmentViewModel: CheckmentViewModel = ViewModelProviders.of(context as FragmentActivity).get(
//                    CheckmentViewModel::class.java)
//                checkmentViewModel.getselect_boo().observe(context, Observer<Boolean>{ checkment->
//                    if(checkment==true){
//                        image_saved.setImageResource(R.drawable.ic_wb_incandescent_active_24dp)
//                    }
//                    else{
//                        image_saved.setImageResource(R.drawable.ic_wb_incandescent_black_24dp)
//                    }
//                })

                if(scanResult.selected==true){
                    image_saved.setImageResource(R.drawable.ic_wb_incandescent_active_24dp)
                }
                else{
                    image_saved.setImageResource(R.drawable.ic_wb_incandescent_black_24dp)
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

