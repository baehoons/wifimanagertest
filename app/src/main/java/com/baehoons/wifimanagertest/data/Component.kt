package com.baehoons.wifimanagertest.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Component : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name = "ssid_s")
    var ssid_w: String? = null

    @ColumnInfo(name = "bssid_s")
    var bssid_w: String? = null

    @ColumnInfo(name = "selected")
    var selected = false

}