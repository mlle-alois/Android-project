package com.azimmermannrosenthal.myapplication

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class NutritionFacts(
    val energy: NutritionFactsItem,
    val fat: NutritionFactsItem,
    val saturatedFattyAcids: NutritionFactsItem,
    val carbohydrates: NutritionFactsItem,
    val sugars: NutritionFactsItem,
    val dietaryFiber: NutritionFactsItem,
    val protein: NutritionFactsItem,
    val salt: NutritionFactsItem,
    val sodium: NutritionFactsItem
) : Parcelable