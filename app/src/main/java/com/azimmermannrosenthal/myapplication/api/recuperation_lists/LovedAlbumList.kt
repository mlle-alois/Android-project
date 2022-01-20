package com.azimmermannrosenthal.myapplication.api.recuperation_lists

import android.os.Parcelable
import com.azimmermannrosenthal.myapplication.objects.Album
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LovedAlbumList(
    @SerializedName("loved")
    val albumList: List<Album>
) : Parcelable