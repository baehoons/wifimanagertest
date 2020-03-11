package com.baehoons.wifimanagertest.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Checkment::class), version = 1)
abstract class AppDatabase_c : RoomDatabase() {
    abstract fun checkmentDao(): CheckmentDao

    companion object {
        private var INSTANCE: AppDatabase_c? = null

        fun getInstance(context: Context): AppDatabase_c? {
            if (INSTANCE == null) {
                synchronized(AppDatabase_c::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase_c::class.java, "checkment")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}