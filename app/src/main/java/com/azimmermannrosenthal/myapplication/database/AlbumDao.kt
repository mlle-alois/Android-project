package com.azimmermannrosenthal.myapplication.database


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AlbumDao {
    @Query("SELECT * FROM albumtable")
    fun getAll(): List<AlbumTable>

    @Insert
    fun insert(vararg albumTable: AlbumTable)

    @Delete
    fun delete(albumTable: AlbumTable)
}