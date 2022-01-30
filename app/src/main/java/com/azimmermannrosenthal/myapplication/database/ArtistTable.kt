package com.azimmermannrosenthal.myapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ArtistTable(
    @PrimaryKey
    val albumId: String
)