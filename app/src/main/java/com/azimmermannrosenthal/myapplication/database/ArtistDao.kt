package com.azimmermannrosenthal.myapplication.database


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ArtistDao {
    @Query("SELECT * FROM artisttable")
    fun getAll(): List<ArtistTable>

    @Insert
    fun insert(vararg artistTable: ArtistTable)

    @Delete
    fun delete(artistTable: ArtistTable)
}