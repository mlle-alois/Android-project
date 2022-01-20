package com.azimmermannrosenthal.myapplication.api.recuperation_lists

import android.os.Parcelable
import com.azimmermannrosenthal.myapplication.objects.Track
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LovedTrackList(
    @SerializedName("loved")
    val trackList: List<Track>
) : Parcelable