package com.azimmermannrosenthal.myapplication.objects

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Artist(
    val idArtist: String,
    val strArtist: String,
    val strStyle: String,
    val strGenre: String,
    val strMood: String,
    val strBiographyEN: String,
    val strBiographyFR: String,
    val strArtistThumb: String,
    val strCountry: String
) : Parcelable