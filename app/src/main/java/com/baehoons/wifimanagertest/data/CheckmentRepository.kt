package com.baehoons.wifimanagertest.data

import android.app.Application
import androidx.lifecycle.LiveData
import java.lang.Exception

class CheckmentRepository (application: Application){

    private val appDatabase = AppDatabase_c.getInstance(application)!!
    private val checkmentDao:CheckmentDao = appDatabase.checkmentDao()
    private val checkment:LiveData<List<Checkment>> = checkmentDao.getAll()
    private val checkment_set:String = checkmentDao.getselected_ch()
    private val checkment_set_st:String = checkmentDao.getstart()
    private val checkment_set_end:String = checkmentDao.getend()
    private val checkment_set_di:Long = checkmentDao.getdiffer()

    fun getAll():LiveData<List<Checkment>>{
        return checkment
    }

    fun getselect():String{
        return checkment_set
    }

    fun getstart():String{
        return checkment_set_st
    }

    fun getend():String{
        return checkment_set_end
    }

    fun getdiffer():Long{
        return checkment_set_di
    }

    fun setstart(checkment: String, state: Boolean){
        try{
            val thread = Thread(Runnable {
                checkmentDao.setstarttime(checkment,state)
            })
            thread.start()
        }catch (e: Exception){

        }
    }

    fun setend(checkment: String, state: Boolean){
        try{
            val thread = Thread(Runnable {
                checkmentDao.setendtime(checkment,state)
            })
            thread.start()
        }catch (e: Exception){

        }
    }

    fun setdiffer(checkment: Long, state: Boolean){
        try{
            val thread = Thread(Runnable {
                checkmentDao.settimediffer(checkment,state)
            })
            thread.start()
        }catch (e: Exception){

        }
    }

    fun insert(checkment: Checkment){
        try{
            val thread = Thread(Runnable {
                checkmentDao.insert(checkment)
            })
            thread.start()
        }catch (e: Exception){

        }
    }
    fun delete(checkment: Checkment){
        try{
            val thread = Thread(Runnable {
                checkmentDao.delete(checkment)
            })
            thread.start()
        }catch (e: Exception){

        }
    }
    fun deleteAll() {
        try {
            val thread = Thread(Runnable {
                checkmentDao.deleteAll()
            })
            thread.start()
        } catch (e: Exception) {

        }
    }
}