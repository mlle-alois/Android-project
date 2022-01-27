package com.azimmermannrosenthal.myapplication.objects

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Album(
    val idAlbum: String,
    val idArtist: String,
    val strAlbum: String,
    val strArtist: String,
    val intYearReleased: String,
    val strStyle: String,
    val strGenre: String,
    val strDescription: String,
    val strDescriptionEN: String,
    val strDescriptionFR: String,
    val intLoved: String,
    val intScore: String,
    val intScoreVotes: String,
    val strAlbumThumb: String,
) : Parcelable