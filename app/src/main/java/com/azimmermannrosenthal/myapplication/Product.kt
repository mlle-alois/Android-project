package com.azimmermannrosenthal.myapplication

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Product(
    val name: String,
    val mark: String,
    val barcode: String,
    val nutriscore: Nutriscore,
    val image_url: String,
    val quantity: String,
    val sold_in: String,
    val ingredients: String,
    val allegenics_substances: String,
    val additives: String,
    val calories: String,
    val nutritionFacts: NutritionFacts
) : Parcelable // permet de le passer en argument pendant la navigation (sérialization)