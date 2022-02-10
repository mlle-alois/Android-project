package com.azimmermannrosenthal.myapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlbumTable(
    @PrimaryKey
    val albumId: String,
    val strAlbum: String,
    val strArtist: String,
    val strAlbumThumb: String,
)