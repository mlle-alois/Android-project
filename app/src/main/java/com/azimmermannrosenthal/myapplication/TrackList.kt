package com.azimmermannrosenthal.myapplication

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrackList(
    @SerializedName("track")
    val trackList: List<Track>
) : Parcelable