package com.baehoons.wifimanagertest.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ComponentDao{
    @Query("SELECT * FROM component")
    fun getAll(): LiveData<List<Component>>

    @Query("SELECT * FROM component WHERE selected = 1")
    fun getselected():LiveData<List<Component>>

    @Query("SELECT * FROM component WHERE selected = 0")
    fun getunselected():LiveData<List<Component>>

    @Query("UPDATE component SET selected = 1 WHERE bssid_s = :titles")
    fun setselected(titles: String)

    @Query("UPDATE component SET selected = 0 WHERE selected = :state")
    fun setunselected(state: Boolean)


    @Insert
    fun insert(component: Component)

    @Delete
    fun delete(component: Component)

    @Query("DELETE FROM component")
    fun deleteAll()

    @Update
    fun update(component: Component)
}