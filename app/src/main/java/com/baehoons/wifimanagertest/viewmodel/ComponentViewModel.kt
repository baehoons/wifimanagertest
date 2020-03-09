package com.baehoons.wifimanagertest.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.baehoons.wifimanagertest.data.Component
import com.baehoons.wifimanagertest.data.ComponentRepository

class ComponentViewModel (application: Application) : AndroidViewModel(application) {

    private val repository = ComponentRepository(application)
    private val component = repository.getAll()

    fun getAll():LiveData<List<Component>>{
        return this.component
    }

    fun insert(component: Component){
        repository.insert(component)
    }

    fun delete(component: Component){
        repository.delete(component)
    }

    fun deleteAll(){
        repository.deleteAll()
    }
}