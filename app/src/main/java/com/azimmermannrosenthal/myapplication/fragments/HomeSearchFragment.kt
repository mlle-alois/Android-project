package com.azimmermannrosenthal.myapplication.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.azimmermannrosenthal.myapplication.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeSearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_home_search,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).findViewById<TextView>(R.id.title).text = getString(R.string.tab_research)

        (activity as AppCompatActivity).findViewById<BottomNavigationView>(R.id.home_nav).visibility = View.VISIBLE

        /*val product = ProductDetailsFragmentArgs.fromBundle(
            requireParentFragment().requireParentFragment().requireArguments()
        ).product*/

        /*this.setGoodNutrientLevel(
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
        )*/
    }

    fun setGoodNutrientLevel(
        colorView: View,
        levelTextView: TextView,
        context: Context,
        quantity: Double,
        max: Int
    ) {
        /*if (quantity < max * 0.2) {
            DrawableCompat.setTintList(
                colorView.background,
                //ColorStateList.valueOf(ContextCompat.getColor(context, R.color.nutrient_level_low))
            )
            levelTextView.setText(R.string.nutrient_level_low)
        } else if (quantity < max) {
            DrawableCompat.setTintList(
                colorView.background,
                //ColorStateList.valueOf(ContextCompat.getColor(context, R.color.nutrient_level_moderate))
            )
            levelTextView.setText(R.string.nutrient_level_moderate)
        } else {
            DrawableCompat.setTintList(
                colorView.background,
                //ColorStateList.valueOf(ContextCompat.getColor(context, R.color.nutrient_level_high))
            )
            levelTextView.setText(R.string.nutrient_level_high)
        }*/
    }
}