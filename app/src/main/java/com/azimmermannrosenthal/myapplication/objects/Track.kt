package com.azimmermannrosenthal.myapplication.objects

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Track(
    val idTrack: String,
    val idAlbum: String,
    val idArtist: String,
    val idLyric: String,
    val strTrack: String,
    val strAlbum: String,
    val strArtist: String,
    val strGenre: String,
    //TODO ajouter éventuels champs manquants / enlever ceux inutiles
) : Parcelable