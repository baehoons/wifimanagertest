package com.baehoons.wifimanagertest.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.baehoons.wifimanagertest.data.Component
import com.baehoons.wifimanagertest.data.ComponentRepository

class ComponentViewModel (application: Application) : AndroidViewModel(application) {

    private val repository = ComponentRepository(application)
    private val component = repository.getAll()
    private val components_sel = repository.getselected()
    private val components_unsel = repository.getunselected()

    fun getAll():LiveData<List<Component>>{
        return this.component
    }

    fun getselect_id():LiveData<String>{
        return this.repository.id_select()
    }

    fun getselect_boo():LiveData<Boolean>{
        return this.repository.id_boo()
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

    fun getselected():LiveData<List<Component>>{
        return this.components_sel
    }

    fun getunselected():LiveData<List<Component>>{
        return this.components_unsel
    }

    fun setselected(component: String){
        repository.setselected(component)
    }

    fun setunselected(state:Boolean){
        repository.setunselected(state)
    }
}