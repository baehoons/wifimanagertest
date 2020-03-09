package com.baehoons.wifimanagertest.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ComponentDao{
    @Query("SELECT * FROM component")
    fun getAll(): LiveData<List<Component>>

    @Insert
    fun insert(component: Component)

    @Delete
    fun delete(component: Component)

    @Query("DELETE FROM component")
    fun deleteAll()

    @Update
    fun update(component: Component)
}