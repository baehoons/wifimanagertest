package com.baehoons.wifimanagertest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {

    private val fragmentmain = MainFragment()
    private val fragmentresult = ResultFragment()
    private val fragmentsetting = SettingFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomnav = findViewById<View>(R.id.bottom) as BottomNavigationView
        bottomnav.setOnNavigationItemSelectedListener(this)

        val navController = Navigation.findNavController(this,R.id.nav_host_fragment)

        NavigationUI.setupWithNavController(bottomnav,navController)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.main ->{
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment,fragmentmain).commit()
            }

            R.id.result ->{
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment,fragmentresult).commit()
            }

            R.id.setting->{
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment,fragmentsetting).commit()
            }
        }
        return true
    }
}
