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
import androidx.work.ExistingWorkPolicy.REPLACE
import com.baehoons.wifimanagertest.R
import com.baehoons.wifimanagertest.utils.ExampleJobService
import com.baehoons.wifimanagertest.utils.ScanWorker
import com.baehoons.wifimanagertest.utils.ScanWorker.Companion.NOTIFICATION_ID
import com.baehoons.wifimanagertest.utils.ScanWorker.Companion.NOTIFICATION_WORK
import com.baehoons.wifimanagertest.utils.setupWithNavController
import com.baehoons.wifimanagertest.viewmodel.CheckmentViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_main.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null
    lateinit var mThread: Thread
    private var mCount: Int = 0
    private var backkeytime: Long = 0
    private val TAG = "MainActivity"
    private var sum: Int = 0
    private lateinit var jobScheduler: JobScheduler
    private lateinit var jobInfo: JobInfo
    lateinit var toast: Toast
    private lateinit var checkmentViewModel: CheckmentViewModel
    private lateinit var workId: UUID
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkmentViewModel = ViewModelProviders.of(this).get(CheckmentViewModel::class.java)
        val componentName = ComponentName(applicationContext, ExampleJobService::class.java)


        val alert_ex = AlertDialog.Builder(this)
        alert_ex
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
        //scheduleJob()
        startSimpleWork()
        cancelWork()
//        var boo:Boolean = intent.getBooleanExtra("boo",false)
//        if(boo==true){
//            Log.d("boo","connected!!")
//        }
    }

    private fun startSimpleWork() {
        checkmentViewModel.getselect().observe(this, Observer<String> { checkment ->
            checkmentViewModel.getstart().observe(this, Observer<String> { checkmente ->
                checkmentViewModel.getend().observe(this, Observer<String> { checkmentes ->
                    //create input data
                    val inputData = workDataOf(
                        Pair("ssid", checkment),
                        Pair(NOTIFICATION_ID, 0),
                        Pair("noti", checkmente),
                        Pair("noti_e",checkmentes)
                    )

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
                        .setInitialDelay(3, TimeUnit.SECONDS)
                        .build()

//            .setConstraints(constraint)
//            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS) //retry work conditions
//            .setInitialDelay(3, TimeUnit.SECONDS)\

                    WorkManager.getInstance()
                        .beginUniqueWork(NOTIFICATION_WORK, REPLACE, workRequest)
                        .enqueue()
                    workId = workRequest.id

                    WorkManager.getInstance().getWorkInfoByIdLiveData(workId)
                        .observe(this, Observer { workInfo ->
                            if (workInfo != null) {
                                if (workInfo.state == WorkInfo.State.ENQUEUED) {
                                    // Show the work state in text view
//                                    Toast.makeText(
//                                        this,
//                                        "WorkInfo.State.ENQUEUED.",
//                                        Toast.LENGTH_LONG
//                                    )
//                                        .show()
                                    Log.d("me", "중간1")
                                } else if (workInfo.state == WorkInfo.State.BLOCKED) {
//                                    Toast.makeText(
//                                        this,
//                                        "WorkInfo.State.BLOCKED.",
//                                        Toast.LENGTH_LONG
//                                    )
//                                        .show()
                                    Log.d("me", "중간2")
                                } else if (workInfo.state == WorkInfo.State.RUNNING) {
//                                    Toast.makeText(
//                                        this,
//                                        "WorkInfo.State.RUNNING.",
//                                        Toast.LENGTH_LONG
//                                    )
//                                        .show()
                                    Log.d("me", "중간3")
                                }

                            }

                            // When work finished
                            if (workInfo != null && workInfo.state.isFinished) {
                                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
//                                    Toast.makeText(
//                                        this,
//                                        "WorkInfo.State.SUCCEEDED.",
//                                        Toast.LENGTH_LONG
//                                    )
//                                        .show()
                                    Log.d("me", "끝")
                                    // Get the output data
                                    val successOutputData = workInfo.outputData
                                    Log.d("me", successOutputData.toString())
                                    val hh = successOutputData.getBoolean("start", false)
                                    var hh_e = successOutputData.getBoolean("end",true)
                                    Log.d("me", hh.toString())

                                    if (checkmente == null) {
                                        if (hh == true) {
                                            checkmentViewModel.setstart(timenow(), true)
                                        }
                                    }

                                    if(checkmentes==null){
                                        if(checkmente != null){
                                            if(hh_e==false){
                                                checkmentViewModel.setend(timenow(), true)


                                            }
                                        }
                                    }


                                } else if (workInfo.state == WorkInfo.State.FAILED) {
//                                    Toast.makeText(
//                                        this,
//                                        "WorkInfo.State.FAILED.",
//                                        Toast.LENGTH_LONG
//                                    )
//                                        .show()
                                    Log.d("me", "실패")
                                } else if (workInfo.state == WorkInfo.State.CANCELLED) {
//                                    Toast.makeText(
//                                        this,
//                                        "WorkInfo.State.CANCELLED.",
//                                        Toast.LENGTH_LONG
//                                    )
//                                        .show()
                                    Log.d("me", "캔슬")
                                }
                            }
                        })
                })
            })
        })
    }

    private fun createWorkRequest() {


    }


    private fun observeWorkStatus() {
        WorkManager.getInstance().getWorkInfoByIdLiveData(workId)
            .observe(this, Observer { workInfo ->
                if (workInfo.state == null) {
                    return@Observer
                }
                when (workInfo.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        Log.d("me", "succed")
                        var ss = workInfo.outputData

                    }
                }
                //do some action based on workInfo state
            })
    }

    fun timenow(): String {
        val realformat: String = "yyyyMMddHHmm"
        var sdfs = SimpleDateFormat(realformat, Locale.KOREA)
        val timenowz = sdfs.format(Date())
        return timenowz
    }

    fun scheduleJob() {
        checkmentViewModel.getselect().observe(this, Observer<String> { checkment ->
            if (checkment != null) {
                var bundles = Bundle()
                bundles.putBoolean("select", true)

                var bundle = PersistableBundle()
                bundle.putString("ssid", checkment)
                Log.d("sss", checkment.trim())
                Log.d("sss", "gogo")
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

                checkmentViewModel.getstart()
                    .observe(this, Observer<String> { checkmente ->
                        if (checkmente == null) {
                            checkmentViewModel.setstart(timenow(), true)

                        }
                    })

            }

//            else{
//                sum++
//                Log.d(TAG,"sum값 $sum")
//                if(sum>5){
//                    checkmentViewModel.getend().observe(this, Observer<String> { checkments ->
//                        if (checkments == null) {
//                            checkmentViewModel.setend(timenow(), true)
//
//                        }
//                    })
//                }
//            }
        })


    }

    fun cancelJob() {
        jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancelAll()
        Log.d(TAG, "Job cancelled")
    }

    fun cancelWork() {
        checkmentViewModel.getend().observe(this, Observer<String> { checkment2 ->
            if (checkment2 != null) {
                WorkManager.getInstance().cancelAllWork()
                Log.d("canceled", "job all canceled")
            }
        })
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
        Log.d("ScanWorker", "종료됌")

        //cancelJob()
        super.onDestroy()
    }
}
