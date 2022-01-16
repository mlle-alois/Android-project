package com.azimmermannrosenthal.myapplication

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class NutritionFactsItem(
    val unity: String,
    val quantityPer100g: Double,
    val quantityPerPortion: Double
) : Parcelable