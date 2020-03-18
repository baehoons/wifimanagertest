package com.baehoons.wifimanagertest.view

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.baehoons.wifimanagertest.R
import com.baehoons.wifimanagertest.utils.ExampleJobService
import com.baehoons.wifimanagertest.utils.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null
    lateinit var mThread: Thread
    private var mCount:Int = 0
    private var backkeytime:Long = 0
    private val TAG = "MainActivity"
    private lateinit var jobScheduler: JobScheduler
    private lateinit var jobInfo:JobInfo
    lateinit var toast : Toast
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val alert_ex = AlertDialog.Builder(this)
        alert_ex
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
        scheduleJob()

        //startService(Intent(applicationContext, ComService::class.java))
    }

    fun startThread(view: View){
        Thread(object : Runnable {
            override fun run() {
                for(i in 0..99){
                    try {
                        mCount++
                    } catch (e:InterruptedException){
                        break
                    }
                    Log.d("my thread","스레드 동작중"+mCount)
                }
            }

        })

    }

    fun scheduleJob() {
        val componentName = ComponentName(this, ExampleJobService::class.java)
        val info = JobInfo.Builder(123, componentName)
            .setRequiresCharging(true)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPersisted(true)
            .setPeriodic(15 * 60 * 1000)
        jobInfo = info.build()
        jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        val resultCode = jobScheduler.schedule(jobInfo)
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled")
        } else {
            Log.d(TAG, "Job scheduling failed")
        }
    }

    fun cancelJob() {
        jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancel(123)
        Log.d(TAG, "Job cancelled")
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

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onDestroy() {
        Log.d("JobService","스탑댐")
        cancelJob()
        super.onDestroy()
    }
}
