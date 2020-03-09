package com.baehoons.wifimanagertest.data

import android.app.Application
import androidx.lifecycle.LiveData
import java.lang.Exception

class ComponentRepository (application: Application){
    private val appDatabase = AppDatabase.getInstance(application)!!
    private val componentDao:ComponentDao = appDatabase.componentDao()
    private val component:LiveData<List<Component>> = componentDao.getAll()

    fun getAll():LiveData<List<Component>>{
        return component
    }

    fun insert(component: Component){
        try{
            val thread = Thread(Runnable {
                componentDao.insert(component)
            })
            thread.start()
        }catch (e:Exception){

        }
    }
    fun delete(component: Component){
        try{
            val thread = Thread(Runnable {
                componentDao.delete(component)
            })
            thread.start()
        }catch (e:Exception){

        }
    }
    fun deleteAll(){
        try{
            val thread = Thread(Runnable {
                componentDao.deleteAll()
            })
            thread.start()
        }catch (e:Exception){

        }
    }
}