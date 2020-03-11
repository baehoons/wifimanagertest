package com.baehoons.wifimanagertest.view.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.baehoons.wifimanagertest.R
import com.baehoons.wifimanagertest.data.Checkment
import com.baehoons.wifimanagertest.viewmodel.CheckmentViewModel
import com.baehoons.wifimanagertest.viewmodel.ComponentViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {
    private lateinit var checkmentViewModel: CheckmentViewModel
    private val realformat:String ="yyyyMMddHHmm"
    private var sdfs = SimpleDateFormat(realformat, Locale.KOREA)
    lateinit var ssid_name:String

    fun getDifferTime(start:String, end:String){

        //private val timess = sdfs.format(Date())
        //    private val timesss = "202003111905"  이런형식
        val startDate = sdfs.parse(start)
        val endDate = sdfs.parse(end)
        var diff = (endDate.time - startDate.time)/(60*1000)
        var hour = diff/60
        var minute = diff%60
        //분을 나타냄
        Log.d("ju",diff.toString())
        Log.d("ju",hour.toString())
        Log.d("ju",minute.toString())

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkmentViewModel = ViewModelProviders.of(this).get(CheckmentViewModel::class.java)
        checkmentViewModel.getAll().observe(viewLifecycleOwner, Observer<List<Checkment>>{ chekment->
            Log.d("tts",chekment.toString())
        })

        if(checkmentViewModel.getselect()==null){
            text_wifistate.text = "와이파이가 연결되어 있지 않습니다 와이파이를 검색해주세요"
        }
        else{
            ssid_name = checkmentViewModel.getselect()
            text_wifistate.text = "$ssid_name 이 연결되어 있습니다"
        }

        if(checkmentViewModel.getstart()==null){
            startings.text = "아직 출근을 안하였습니다."
        }
        else{
            var st = checkmentViewModel.getstart()
            startings.text = " $st "
        }

        if(checkmentViewModel.getend()==null){
            endings.text = "아직 퇴근을 안하였습니다."
        }
        else{
            var ed = checkmentViewModel.getend()
            endings.text = " $ed "
        }

        if(checkmentViewModel.getdiffer().equals(0)){
            status.text = "출근과 퇴근을 해야 시간이 측정됩니다."
        }
        else{
            var hour = checkmentViewModel.getdiffer()/60
            var minute = checkmentViewModel.getdiffer()%60
            if(hour>0){
                status.text="오늘은 $hour 시간  $minute 분  근무 하였습니다"
            }
            else{
                status.text="오늘은  $minute 분  근무 하였습니다"
            }

        }
        startings.text

        wifisetting.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_controlFragment)
        }



    }
}
