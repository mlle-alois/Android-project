package com.azimmermannrosenthal.myapplication.api.recuperation_lists

import android.os.Parcelable
import com.azimmermannrosenthal.myapplication.objects.Artist
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FoundedArtistList(
    @SerializedName("artists")
    val list: List<Artist>
) : Parcelable