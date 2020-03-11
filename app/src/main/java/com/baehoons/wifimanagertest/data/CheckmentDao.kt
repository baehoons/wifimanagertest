package com.baehoons.wifimanagertest.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CheckmentDao {
    @Query("SELECT * FROM checkment")
    fun getAll(): LiveData<List<Checkment>>

    @Query("SELECT ssid_set FROM checkment WHERE selected = 1")
    fun getselected_ch():String

    @Query("SELECT start_time FROM checkment WHERE selected = 1")
    fun getstart():String

    @Query("SELECT end_time FROM checkment WHERE selected = 1")
    fun getend():String

    @Query("SELECT time_differ FROM checkment WHERE selected = 1")
    fun getdiffer():Long


    @Query("UPDATE checkment SET start_time = :starting_time WHERE selected = :state")
    fun setstarttime(starting_time:String, state:Boolean)

    @Query("UPDATE checkment SET end_time = :ending_time WHERE selected = :state")
    fun setendtime(ending_time:String, state:Boolean)

    @Query("UPDATE checkment SET time_differ = :timediffer WHERE selected = :state")
    fun settimediffer(timediffer:Long, state:Boolean)

//    @Query("UPDATE checkment SET selected = 1 WHERE ssid_set = :titles")
//    fun setselected(titles: String)
//
//    @Query("UPDATE checkment SET selected = 0 WHERE selected = :state")
//    fun setunselected(state: Boolean)


    @Insert
    fun insert(checkment: Checkment)

    @Delete
    fun delete(checkment: Checkment)

    @Query("DELETE FROM Checkment")
    fun deleteAll()

    @Update
    fun update(checkment: Checkment)
}