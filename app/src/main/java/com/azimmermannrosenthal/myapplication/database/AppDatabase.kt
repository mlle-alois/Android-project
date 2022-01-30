package com.azimmermannrosenthal.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AlbumTable::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
}