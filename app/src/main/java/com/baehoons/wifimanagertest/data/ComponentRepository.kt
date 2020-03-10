package com.baehoons.wifimanagertest.data

import android.app.Application
import androidx.lifecycle.LiveData
import java.lang.Exception
import kotlin.time.measureTime

class ComponentRepository (application: Application){
    private val appDatabase = AppDatabase.getInstance(application)!!
    private val componentDao:ComponentDao = appDatabase.componentDao()
    private val component:LiveData<List<Component>> = componentDao.getAll()
    private val components_sel:LiveData<List<Component>> = componentDao.getselected()
    private val components_unsel:LiveData<List<Component>> = componentDao.getunselected()


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

    fun getselected():LiveData<List<Component>>{
        return components_sel
    }
    fun getunselected():LiveData<List<Component>>{
        return components_unsel
    }

    fun setselected(component: String){
        try{
            val thread = Thread(Runnable {
                componentDao.setselected(component)
            })
            thread.start()
        }catch (e:Exception){

        }
    }

    fun setunselected(state :Boolean){
        try{
            val thread = Thread(Runnable {
                componentDao.setunselected(state)
            })
            thread.start()
        }catch (e:Exception){

        }
    }
}