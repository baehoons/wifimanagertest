package com.baehoons.wifimanagertest.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.baehoons.wifimanagertest.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    var navController: NavController?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        wifisetting.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_controlFragment)
        }



    }
}