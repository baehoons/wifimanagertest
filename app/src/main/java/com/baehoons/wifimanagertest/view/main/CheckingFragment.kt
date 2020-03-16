package com.baehoons.wifimanagertest.view.main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.baehoons.wifimanagertest.R
import com.baehoons.wifimanagertest.viewmodel.CheckmentViewModel
import kotlinx.android.synthetic.main.fragment_checking.*
import java.text.SimpleDateFormat
import java.util.*


class CheckingFragment : Fragment() {
    private lateinit var checkmentViewModel: CheckmentViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checking, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkmentViewModel = ViewModelProviders.of(this).get(CheckmentViewModel::class.java)

        checkmentViewModel.getstart().observe(viewLifecycleOwner, Observer<String> { checkment ->

            if (checkment == null) {
                st.text = "아직 출근을 안하셨습니다."
            } else {
                val stt = checkment.trim()
                val da = SimpleDateFormat("yyyyMMddHHmm")
                val da2 = ("yyyy년 MM월 dd일 HH시 mm분")
                val sdf = SimpleDateFormat(da2, Locale.KOREA)
                var date_s = da.parse(stt)
                val stt2 = sdf.format(date_s)
                st.text = stt2
            }
        })

        checkmentViewModel.getend().observe(viewLifecycleOwner, Observer<String>{checkment->

            if(checkment == null){
                ed.text = "아직 퇴근을 안하셨습니다."
            }
            else{
                val stt = checkment.trim()
                val da = SimpleDateFormat("yyyyMMddHHmm")
                val da2 = ("yyyy년 MM월 dd일 HH시 mm분")
                val sdf = SimpleDateFormat(da2, Locale.KOREA)
                var date_s = da.parse(stt)
                val stt2 = sdf.format(date_s)
                ed.text = stt2
            }
        })

        checkmentViewModel.getdiffer().observe(viewLifecycleOwner, Observer<String>{checkment ->
            if(checkment ==null){
                tt.text = "퇴근을 해야 측정이 가능합니다."
            }
            else{
                tt.text = checkment
            }

        })
    }

}
