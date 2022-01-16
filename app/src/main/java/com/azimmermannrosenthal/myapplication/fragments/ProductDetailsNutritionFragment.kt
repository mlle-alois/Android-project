package com.azimmermannrosenthal.myapplication.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.azimmermannrosenthal.myapplication.R
import kotlinx.android.synthetic.main.fragment_product_details_summary.view.*

class ProductDetailsNutritionFragment : Fragment() {

    val MAX_OF_FAT_IN_GRAMS = 15;
    val MAX_OF_SATURATED_FATTY_ACIDS_IN_GRAMS = 4;
    val MAX_OF_SUGAR_IN_GRAMS = 20;
    val MAX_OF_SALT_IN_GRAMS = 1;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_product_details_nutrition_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = ProductDetailsFragmentArgs.fromBundle(
            requireParentFragment().requireParentFragment().requireArguments()
        ).product

        view.findViewById<TextView>(R.id.product_fat).text = getString(
            R.string.product_fat,
            product.nutritionFacts.fat.quantityPer100g.toString(),
            product.nutritionFacts.fat.unity
        )
        view.findViewById<TextView>(R.id.product_saturated_fatty_acids).text = getString(
            R.string.product_saturated_fatty_acids,
            product.nutritionFacts.saturatedFattyAcids.quantityPer100g.toString(),
            product.nutritionFacts.saturatedFattyAcids.unity
        )
        view.findViewById<TextView>(R.id.product_sugars).text = getString(
            R.string.product_sugars,
            product.nutritionFacts.sugars.quantityPer100g.toString(),
            product.nutritionFacts.sugars.unity
        )
        view.findViewById<TextView>(R.id.product_salt).text = getString(
            R.string.product_salt,
            product.nutritionFacts.salt.quantityPer100g.toString(),
            product.nutritionFacts.salt.unity
        )

        this.setGoodNutrientLevel(
            view.findViewById(R.id.product_fat_color),
            view.findViewById(R.id.product_fat_quantity),
            view.context,
            product.nutritionFacts.fat.quantityPer100g,
            MAX_OF_FAT_IN_GRAMS
        )
        this.setGoodNutrientLevel(
            view.findViewById(R.id.product_saturated_fatty_acids_color),
            view.findViewById(R.id.product_saturated_fatty_acids_quantity),
            view.context,
            product.nutritionFacts.saturatedFattyAcids.quantityPer100g,
            MAX_OF_SATURATED_FATTY_ACIDS_IN_GRAMS
        )
        this.setGoodNutrientLevel(
            view.findViewById(R.id.product_sugars_color),
            view.findViewById(R.id.product_sugars_quantity),
            view.context,
            product.nutritionFacts.sugars.quantityPer100g,
            MAX_OF_SUGAR_IN_GRAMS
        )
        this.setGoodNutrientLevel(
            view.findViewById(R.id.product_salt_color),
            view.findViewById(R.id.product_salt_quantity),
            view.context,
            product.nutritionFacts.salt.quantityPer100g,
            MAX_OF_SALT_IN_GRAMS
        )
    }

    fun setGoodNutrientLevel(
        colorView: View,
        levelTextView: TextView,
        context: Context,
        quantity: Double,
        max: Int
    ) {
        if (quantity < max * 0.2) {
            DrawableCompat.setTintList(
                colorView.background,
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.nutrient_level_low))
            )
            levelTextView.setText(R.string.nutrient_level_low)
        } else if (quantity < max) {
            DrawableCompat.setTintList(
                colorView.background,
                ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.nutrient_level_moderate)
                )
            )
            levelTextView.setText(R.string.nutrient_level_moderate)
        } else {
            DrawableCompat.setTintList(
                colorView.background,
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.nutrient_level_high))
            )
            levelTextView.setText(R.string.nutrient_level_high)
        }
    }
}