package com.baehoons.wifimanagertest.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Checkment : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name = "ssid_set")
    var ssid_set: String? = null

    @ColumnInfo(name = "start_time")
    var start_time: String? = null

    @ColumnInfo(name = "end_time")
    var end_time: String? = null

    @ColumnInfo(name = "time_differ")
    var time_differ:Long = 0

    @ColumnInfo(name = "selected")
    var selected = false

}