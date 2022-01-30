package com.azimmermannrosenthal.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        AlbumTable::class,
        ArtistTable::class
    ], version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
    abstract fun artistDao(): ArtistDao
}