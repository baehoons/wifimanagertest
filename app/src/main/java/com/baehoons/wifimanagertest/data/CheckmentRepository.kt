package com.baehoons.wifimanagertest.data

import android.app.Application
import androidx.lifecycle.LiveData
import java.lang.Exception

class CheckmentRepository (application: Application){
    private val appDatabase_c = AppDatabase_c.getInstance(application)!!
    private val checkmentDao:CheckmentDao = appDatabase_c.checkmentDao()
    private val checkment:LiveData<List<Checkment>> = checkmentDao.getAll()
    private val checkment_set:LiveData<String> = checkmentDao.getselected_ch()
    private val checkment_set_st:LiveData<String> = checkmentDao.getstart()
    private val checkment_set_end:LiveData<String> = checkmentDao.getend()
    private val checkment_set_di:LiveData<String> = checkmentDao.getdiffer()

    fun getAll():LiveData<List<Checkment>>{
        return checkment
    }

    fun id_boo():LiveData<Boolean>{
        return checkmentDao.getselected_boo()
    }

    fun getselect():LiveData<String>{
        return checkment_set
    }

    fun getstart():LiveData<String>{
        return checkment_set_st
    }

    fun getend():LiveData<String>{
        return checkment_set_end
    }

    fun getdiffer():LiveData<String>{
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

    fun setdiffer(checkment: String, state: Boolean){
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