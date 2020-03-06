package com.baehoons.wifimanagertest.data

import androidx.room.*

@Dao
interface ComponentDao{
    @Query("SELECT * FROM component")
    fun getAll(): List<Component>

    @Insert
    fun insert(component: Component)

    @Delete
    fun delete(component: Component)

    @Update
    fun update(component: Component)
}