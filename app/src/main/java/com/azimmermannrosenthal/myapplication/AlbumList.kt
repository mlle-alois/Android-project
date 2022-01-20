package com.azimmermannrosenthal.myapplication

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class AlbumList(
    @SerializedName("album")
    val albumList: List<Album>
) : Parcelable