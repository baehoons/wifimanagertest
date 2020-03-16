package com.baehoons.wifimanagertest.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.baehoons.wifimanagertest.data.Checkment
import com.baehoons.wifimanagertest.data.CheckmentRepository

class CheckmentViewModel (application: Application) : AndroidViewModel(application) {

    private val repositoies = CheckmentRepository(application)
    private val checkment = repositoies.getAll()
    private val checkment_get = repositoies.getselect()
    private val checkment_get_st = repositoies.getstart()
    private val checkment_get_en = repositoies.getend()
    private val checkment_get_di = repositoies.getdiffer()


    fun getAll():LiveData<List<Checkment>>{
        return this.checkment
    }

    fun getselect_boo():LiveData<Boolean>{
        return this.repositoies.id_boo()
    }


    fun getselect():LiveData<String>{
        return this.checkment_get
    }

    fun getstart():LiveData<String>{
        return this.checkment_get_st
    }

    fun getend():LiveData<String>{
        return this.checkment_get_en
    }

    fun getdiffer():LiveData<String>{
        return this.checkment_get_di
    }

    fun setstart(checkment: String, state:Boolean){
        repositoies.setstart(checkment, state)
    }

    fun setend(checkment: String, state:Boolean){
        repositoies.setend(checkment, state)
    }

    fun setdiffer(checkment: String, state:Boolean){
        repositoies.setdiffer(checkment, state)
    }

    fun insert(checkment: Checkment){
        repositoies.insert(checkment)
    }

    fun delete(checkment: Checkment){
        repositoies.delete(checkment)
    }

    fun deleteAll(){
        repositoies.deleteAll()
    }
}