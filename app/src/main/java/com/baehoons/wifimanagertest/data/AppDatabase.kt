package com.baehoons.wifimanagertest.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Component::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun componentDao(): ComponentDao
}