package com.baehoons.wifimanagertest.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.baehoons.wifimanagertest.R
import com.baehoons.wifimanagertest.utils.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null
    private var backkeytime:Long = 0
    lateinit var toast : Toast
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val alert_ex = AlertDialog.Builder(this)
        alert_ex
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        val navGraphIds = listOf(
            R.navigation.main_navigations,
            R.navigation.result_navigation,
            R.navigation.setting_navigation
        )

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() > backkeytime + 2000) {
            backkeytime = System.currentTimeMillis()
            toast = Toast.makeText(this, "나가려면 뒤로가기를 한번 더 누르세요", Toast.LENGTH_LONG)
            toast.show()
            return
        }

        if (System.currentTimeMillis() <= backkeytime + 2000) {
            finish()
            toast.cancel()
        }
    }

}
