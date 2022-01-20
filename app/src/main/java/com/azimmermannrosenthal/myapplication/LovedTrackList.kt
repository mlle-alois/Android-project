package com.azimmermannrosenthal.myapplication

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LovedTrackList(
    @SerializedName("loved")
    val trackList: List<Track>
) : Parcelable