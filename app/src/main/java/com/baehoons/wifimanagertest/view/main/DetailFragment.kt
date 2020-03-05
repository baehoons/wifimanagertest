package com.baehoons.wifimanagertest.view.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.baehoons.wifimanagertest.R
import kotlinx.android.synthetic.main.fragment_detail.*
import java.text.SimpleDateFormat
import java.util.*


class DetailFragment : Fragment() {

    lateinit var ssid :String
    lateinit var bssid :String
    lateinit var level :String
    lateinit var frequency :String
    lateinit var capabilities :String
    private val format:String ="yyyy년 MM월 dd일 HH시 mm분"
    private var sdf = SimpleDateFormat(format, Locale.KOREA)
    private val times = sdf.format(Date())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ssid = arguments!!.getString("ssid").toString()
        bssid=arguments!!.getString("bssid").toString()
        level=arguments!!.getString("level").toString()
        frequency=arguments!!.getString("frequency").toString()
        capabilities=arguments!!.getString("capabilities").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ssid_name.text = ssid
        bssid_name.text = bssid
        level_name.text = level
        frequency_name.text = frequency
        capabilities_name.text = capabilities
        timenow.text = times

        Log.d("joon", sdf.toString())
        Log.d("joon", times)
        button.setOnClickListener {
            findNavController().popBackStack(R.id.wifiListFragment,true)
        }

    }

}
