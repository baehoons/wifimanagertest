package com.baehoons.wifimanagertest.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.baehoons.wifimanagertest.data.Checkment
import com.baehoons.wifimanagertest.data.CheckmentRepository

class CheckmentViewModel (application: Application):AndroidViewModel(application){

    private val repository = CheckmentRepository(application)
    private val checkment = repository.getAll()
    private val checkment_get = repository.getselect()
    private val checkment_get_st = repository.getstart()
    private val checkment_get_en = repository.getend()
    private val checkment_get_di = repository.getdiffer()


    fun getAll():LiveData<List<Checkment>>{
        return this.checkment
    }

    fun getselect():String{
        return this.checkment_get
    }

    fun getstart():String{
        return this.checkment_get_st
    }

    fun getend():String{
        return this.checkment_get_en
    }

    fun getdiffer():Long{
        return this.checkment_get_di
    }

    fun setstart(checkment: String, state:Boolean){
        repository.setstart(checkment, state)
    }

    fun setemd(checkment: String, state:Boolean){
        repository.setend(checkment, state)
    }

    fun setdiffer(checkment: Long, state:Boolean){
        repository.setdiffer(checkment, state)
    }

    fun insert(checkment: Checkment){
        repository.insert(checkment)
    }

    fun delete(checkment: Checkment){
        repository.delete(checkment)
    }

    fun deleteAll(){
        repository.deleteAll()
    }
}