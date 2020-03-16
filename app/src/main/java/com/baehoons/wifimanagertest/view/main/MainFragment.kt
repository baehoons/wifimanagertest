package com.baehoons.wifimanagertest.view.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
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
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class MainFragment : Fragment() {
    private lateinit var checkmentViewModel: CheckmentViewModel
    lateinit var started: String
    lateinit var ended: String


    fun getDifferTime(start: String, end: String): String {
        //start = sdfs.format(Date())
        //private val timess = sdfs.format(Date())
        //    private val timesss = "202003111905"  이런형식
        val realformat: String = "yyyyMMddHHmm"
        var sdfs = SimpleDateFormat(realformat, Locale.KOREA)
        val startDate = sdfs.parse(start)
        val endDate = sdfs.parse(end)
        var diff = (endDate.time - startDate.time) / (60 * 1000)
        var hour = diff / 60
        var minute = diff % 60
        var timzz = "$hour 시간  $minute 분"
        val timezzz = hour.toString() + "시간" + minute.toString() + "분"
        //분을 나타냄
        Log.d("ju", diff.toString())
        Log.d("ju", hour.toString())
        Log.d("ju", minute.toString())
        Log.d("ju", timzz)
        Log.d("ju", timezzz)
        return timezzz
    }

    fun timenow(): String {
        val realformat: String = "yyyyMMddHHmm"
        var sdfs = SimpleDateFormat(realformat, Locale.KOREA)
        val timenowz = sdfs.format(Date())
        return timenowz
    }

    fun tim(aa: String): String {
        val realformat: String = "yyyyMMddHHmm"
        val format: String = "yyyy년MM월dd일HH시mm분"
        var sdfs = SimpleDateFormat(realformat, Locale.KOREA)
        var sdf = SimpleDateFormat(format, Locale.KOREA)
        var da = sdfs.parse(aa)
        var ad = sdf.format(da)
        return ad
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
        getDifferTime("202003131020", "202003131120")

        checkmentViewModel = ViewModelProviders.of(this).get(CheckmentViewModel::class.java)
        checkmentViewModel.getAll()
            .observe(viewLifecycleOwner, Observer<List<Checkment>> { chekment ->
                Log.d("tts", chekment.toString())
            })

        checkmentViewModel.getselect().observe(viewLifecycleOwner, Observer<String> { checkment ->
            if (checkment == null) {
                text_wifistate.text = "와이파이가 연결되어 있지 않습니다 와이파이를 검색해주세요"
            } else {
                var ssid_name = checkment
                text_wifistate.text = "$ssid_name 이 연결되어 있습니다"
            }
        })




        checkmentViewModel.getstart().observe(viewLifecycleOwner, Observer<String> { checkment ->
            if (checkment == null) {
                startings.text = "아직 출근을 안하였습니다."
            } else {
                var st = checkment
                startings.text = " $st "
            }
        })


        checkmentViewModel.getend().observe(viewLifecycleOwner, Observer<String> { checkment ->
            if (checkment == null) {
                endings.text = "아직 퇴근을 안하였습니다."
            } else {
                var ed = checkment
                endings.text = " $ed "
            }
        })

        checkmentViewModel.getdiffer().observe(viewLifecycleOwner, Observer<String> { checkment ->
            if (checkment == null) {
                status.text = "출퇴근을 완료해야 시간이 측정됩니다."
            } else {
                status.text = "오늘은 $checkment 근무 하였습니다"
            }
        })

//        if(checkmentViewModel.getdiffer().observe(viewLifecycleOwner, Observer<Long> { checkment->
//                checkment
//            }).equals(0)){
//
//        }
//        else{
//            checkmentViewModel.getdiffer().observe(viewLifecycleOwner, Observer<Long> { checkment->
//
//
//            })
//
//        }

        wifisetting.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_controlFragment)
        }

        in_s.setOnClickListener {
            checkmentViewModel.getstart()
                .observe(viewLifecycleOwner, Observer<String> { checkment ->
                    if (checkment == null) {
                        checkmentViewModel.setstart(timenow(), true)
                        startings.text = checkment
                    }
                })
        }

        out_s.setOnClickListener {
            checkmentViewModel.getend().observe(viewLifecycleOwner, Observer<String> { checkment ->
                if (checkment == null) {
                    checkmentViewModel.setend(timenow(), true)
                    endings.text = checkment

                }
            })

        }

        checkingtime.setOnClickListener {
            checkmentViewModel.getselect().observe(viewLifecycleOwner, Observer<String> { checkment ->
                if(checkment != null){
                    if (startings.text.toString() != "아직 출근을 안하였습니다." && endings.text.toString() != "아직 퇴근을 안하였습니다.") {
                        var started = (startings.text).toString().trim()
                        var ended = (endings.text).toString().trim()
                        val da = SimpleDateFormat("yyyyMMddHHmm")
                        var ff = "202003131350"
                        var date_s = da.parse(started)
                        var date_e = da.parse(ended)

                        var diff = (date_e.time - date_s.time) / (60 * 1000)
                        var timede: String =
                            (diff / 60).toString() + "시간 " + (diff % 60).toString() + "분"
                        Log.d("hhj_s", started)
                        Log.d("hh_f", diff.toString())
                        Log.d("hhj", timede)
                        checkmentViewModel.setdiffer(timede, true)//string을 date 형식으로 함수에 문제점이 있었음
                        status.text


                    }
                    findNavController().navigate(R.id.action_mainFragment_to_checkingFragment)
                }
            })

        }
    }
}
