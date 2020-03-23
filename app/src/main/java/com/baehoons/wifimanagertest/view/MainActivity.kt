package com.baehoons.wifimanagertest.view

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.work.*
import com.baehoons.wifimanagertest.R
import com.baehoons.wifimanagertest.utils.ExampleJobService
import com.baehoons.wifimanagertest.utils.ScanWorker
import com.baehoons.wifimanagertest.utils.setupWithNavController
import com.baehoons.wifimanagertest.viewmodel.CheckmentViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null
    lateinit var mThread: Thread
    private var mCount:Int = 0
    private var backkeytime:Long = 0
    private val TAG = "MainActivity"
    private lateinit var jobScheduler: JobScheduler
    private lateinit var jobInfo:JobInfo
    lateinit var toast : Toast
    private lateinit var checkmentViewModel: CheckmentViewModel
    private lateinit var workId : UUID
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkmentViewModel = ViewModelProviders.of(this).get(CheckmentViewModel::class.java)
        val componentName = ComponentName(applicationContext,ExampleJobService::class.java)


        val alert_ex = AlertDialog.Builder(this)
        alert_ex
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
        //scheduleJob()
        startSimpleWork()
        observeWorkStatus()

//        var boo:Boolean = intent.getBooleanExtra("boo",false)
//        if(boo==true){
//            Log.d("boo","connected!!")
//        }
    }

    private fun startSimpleWork() {
        val workRequest = createWorkRequest()
        WorkManager.getInstance().enqueue(workRequest)
        workId = workRequest.id
    }

    private fun createWorkRequest() : WorkRequest {
        //create input data
        val inputData = workDataOf(Pair("INPUT_KEY", "some_input"))

        //create constraint conditions
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .setRequiresCharging(false)
            .build()

        //init WorkRequest using WorkRequest class or some Builder for single or periodic work
        val workRequest = OneTimeWorkRequestBuilder<ScanWorker>()
            .setInputData(inputData)
            .setConstraints(constraint)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS) //retry work conditions
            .setInitialDelay(3, TimeUnit.SECONDS)
            .build()

        return workRequest
    }

    private fun observeWorkStatus() {
        WorkManager.getInstance().getWorkInfoByIdLiveData(workId)
            .observe(this, Observer { workInfo ->
                //do some action based on workInfo state
            })
    }

    fun scheduleJob() {
        checkmentViewModel.getselect().observe(this, Observer<String> { checkment ->
            var bundle = PersistableBundle()
            bundle.putString("ssid",checkment)
            Log.d("sss",checkment.trim())
            Log.d("sss","gogo")
            val componentName = ComponentName(this, ExampleJobService::class.java)
            val info = JobInfo.Builder(123, componentName)
                .setExtras(bundle)
                .setRequiresCharging(false)
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
        })
    }

    fun cancelJob() {
        jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancelAll()
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
        Log.d("JobService","종료됌")
        //cancelJob()
        super.onDestroy()
    }
}
