package com.baehoons.wifimanagertest.view.main


import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.baehoons.wifimanagertest.R
import com.baehoons.wifimanagertest.adapter.WifiSavedListAdapter
import com.baehoons.wifimanagertest.data.AppDatabase
import com.baehoons.wifimanagertest.data.Checkment
import com.baehoons.wifimanagertest.data.Component
import com.baehoons.wifimanagertest.viewmodel.CheckmentViewModel
import com.baehoons.wifimanagertest.viewmodel.ComponentViewModel
import kotlinx.android.synthetic.main.fragment_control.*

import java.lang.Exception

class ControlFragment : Fragment() {
    private lateinit var componentViewModel: ComponentViewModel
    private lateinit var checkmentViewModel: CheckmentViewModel
    var navController: NavController?=null
    private var roomdb: AppDatabase?=null
    private var componentList = arrayListOf<Component>()
    private var component_settingList = arrayListOf<Component>()
    private var deviceListAdapter: WifiSavedListAdapter = WifiSavedListAdapter().apply {
        onDeviceClickListener = {onDeviceClicked(it)}
    }
    inner class CheckTypesTask : AsyncTask<Void,Void,Void>(){

        val asyncDialog = ProgressDialog(context)
        override fun onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            asyncDialog.setMessage("변경중입니다..")

            asyncDialog.show()
            super.onPreExecute()

        }
        override fun doInBackground(vararg params: Void?): Void? {
            try{
                for(i in 1..5){
                    asyncDialog.progress = i*30
                    Thread.sleep(500)
                }
            }catch (e:InterruptedException){
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            asyncDialog.dismiss()
            super.onPostExecute(result)
        }


    }



    fun onDeviceClicked(device: Component){
        Toast.makeText(activity,"ssid : ${device.ssid_w} , bssid: ${device.bssid_w}, select: ${device.selected}",Toast.LENGTH_SHORT).show()
        val builders = AlertDialog.Builder(ContextThemeWrapper(activity, R.style.AlertDialog))
        builders.setTitle("${device.ssid_w} 로 기준 wifi로 설정")
        builders.setMessage("기존 저장된 데이터는 모두 삭제 됩니다. 변경하시겠습니까?")

        builders.setPositiveButton("확인") { _, _ ->
            checkmentViewModel.deleteAll()
            componentViewModel.setunselected(true)

            componentViewModel.setselected(device.bssid_w!!)
            componentViewModel.getAll().observe(viewLifecycleOwner, Observer<List<Component>> { component ->
                Log.d("allget",component.toString())
                deviceListAdapter.clearDevices()
                deviceListAdapter.addDevice(ArrayList(component))
            })

            saved_list.adapter = deviceListAdapter
            saved_list.layoutManager = LinearLayoutManager(activity)

            val newCom = Checkment()
            newCom.ssid_set = device.ssid_w
            newCom.start_time=null
            newCom.end_time=null
            newCom.time_differ = null
            newCom.selected = true
            checkmentViewModel.insert(newCom)

            val task = CheckTypesTask()
            task.execute()



        }
        builders.setNegativeButton("취소") { _, _ ->

        }

        builders.show()
        
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_control, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        checkmentViewModel = ViewModelProviders.of(this).get(CheckmentViewModel::class.java)
        componentViewModel = ViewModelProviders.of(this).get(ComponentViewModel::class.java)

        componentViewModel.getAll().observe(viewLifecycleOwner, Observer<List<Component>> { component ->
            Log.d("allget",component.toString())
            deviceListAdapter.clearDevices()
            deviceListAdapter.addDevice(ArrayList(component))
        })

        componentViewModel.getselected().observe(viewLifecycleOwner, Observer<List<Component>>{component ->
            Log.d("room_sel",(component).toString())
            component_settingList.clear()
            component_settingList.addAll(ArrayList(component))

        })
        componentViewModel.getunselected().observe(viewLifecycleOwner, Observer<List<Component>>{component ->
            Log.d("room_unsel",(component).toString())
            component_settingList.clear()
            component_settingList.addAll(ArrayList(component))
        })

        saved_list.adapter = deviceListAdapter
        saved_list.layoutManager = LinearLayoutManager(activity)


//        roomdb = AppDatabase.getInstance(context!!)
//        val r = Runnable {
//            try {
//                componentList = ArrayList(roomdb?.componentDao()?.getAll()!!)
//                deviceListAdapter.addDevice(componentList)
//                updateview()
//            }
//            catch (e:Exception){
//                Log.d("errtag","Error - $e")
//            }
//
//        }
//
//        val thread = Thread(r)
//        thread.start()


        button_add_wifi.setOnClickListener {
            findNavController().navigate(R.id.action_controlFragment_to_wifiListFragment)
        }



        deleteimage.setOnClickListener {

            val builder = AlertDialog.Builder(ContextThemeWrapper(activity, R.style.AlertDialog))
            builder.setTitle("리스트 삭제")
            builder.setMessage("리스트 전부를 삭제 하겠습니까?")

            /*builder.setPositiveButton("확인") {dialog, id ->
            }
            builder.setNegativeButton("취소") {dialog, id ->
            }*/
            builder.setPositiveButton("확인") { _, _ ->
                deviceListAdapter.clearDevices()
                componentList.clear()
                componentViewModel.deleteAll()
                checkmentViewModel.deleteAll()
                updateview()
            }
            builder.setNegativeButton("취소") { _, _ ->

            }

            builder.show()

//            val rs = Runnable {
//                try {
//                    roomdb?.componentDao()?.deleteAll()
//                    deviceListAdapter.clearDevices()
//                    componentList.clear()
//                    updateview()
//                }
//                catch (e:Exception){
//                    Log.d("errtag","Error - $e")
//                }
//            }
//            val threads = Thread(rs)
//            threads.start()
        }
    }
    fun updateview(){
        deviceListAdapter.notifyDataSetChanged()
        saved_list.adapter = deviceListAdapter
        saved_list.layoutManager = LinearLayoutManager(activity)
        saved_list.setHasFixedSize(true)
    }



}
