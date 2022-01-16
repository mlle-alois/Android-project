package com.azimmermannrosenthal.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.azimmermannrosenthal.myapplication.R

class ProductDetailsNutritionalValuesFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_product_details_nutritional_values_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = ProductDetailsFragmentArgs.fromBundle(
            requireParentFragment().requireParentFragment().requireArguments()
        ).product

        view.findViewById<TextView>(R.id.energy_quantity_per_100g).text = getString(
            R.string.quantity,
            product.nutritionFacts.energy.quantityPer100g.toString(),
            product.nutritionFacts.energy.unity
        )
        view.findViewById<TextView>(R.id.energy_quantity_per_portion).text = getString(
            R.string.quantity,
            product.nutritionFacts.energy.quantityPerPortion.toString(),
            product.nutritionFacts.energy.unity
        )

        view.findViewById<TextView>(R.id.fat_quantity_per_100g).text = getString(
            R.string.quantity,
            product.nutritionFacts.fat.quantityPer100g.toString(),
            product.nutritionFacts.fat.unity
        )
        view.findViewById<TextView>(R.id.fat_quantity_per_portion).text = getString(
            R.string.quantity,
            product.nutritionFacts.fat.quantityPerPortion.toString(),
            product.nutritionFacts.fat.unity
        )

        view.findViewById<TextView>(R.id.with_saturated_fatty_acids_quantity_per_100g).text = getString(
            R.string.quantity,
            product.nutritionFacts.saturatedFattyAcids.quantityPer100g.toString(),
            product.nutritionFacts.saturatedFattyAcids.unity
        )
        view.findViewById<TextView>(R.id.with_saturated_fatty_acids_quantity_per_portion).text = getString(
            R.string.quantity,
            product.nutritionFacts.saturatedFattyAcids.quantityPerPortion.toString(),
            product.nutritionFacts.saturatedFattyAcids.unity
        )

        view.findViewById<TextView>(R.id.carbohydrates_quantity_per_100g).text = getString(
            R.string.quantity,
            product.nutritionFacts.carbohydrates.quantityPer100g.toString(),
            product.nutritionFacts.carbohydrates.unity
        )
        view.findViewById<TextView>(R.id.carbohydrates_quantity_per_portion).text = getString(
            R.string.quantity,
            product.nutritionFacts.carbohydrates.quantityPerPortion.toString(),
            product.nutritionFacts.carbohydrates.unity
        )

        view.findViewById<TextView>(R.id.with_sugars_quantity_per_100g).text = getString(
            R.string.quantity,
            product.nutritionFacts.sugars.quantityPer100g.toString(),
            product.nutritionFacts.sugars.unity
        )
        view.findViewById<TextView>(R.id.with_sugars_quantity_per_portion).text = getString(
            R.string.quantity,
            product.nutritionFacts.sugars.quantityPerPortion.toString(),
            product.nutritionFacts.sugars.unity
        )

        view.findViewById<TextView>(R.id.dietary_fiber_quantity_per_100g).text = getString(
            R.string.quantity,
            product.nutritionFacts.dietaryFiber.quantityPer100g.toString(),
            product.nutritionFacts.dietaryFiber.unity
        )
        view.findViewById<TextView>(R.id.dietary_fiber_quantity_per_portion).text = getString(
            R.string.quantity,
            product.nutritionFacts.dietaryFiber.quantityPerPortion.toString(),
            product.nutritionFacts.dietaryFiber.unity
        )

        view.findViewById<TextView>(R.id.proteins_quantity_per_100g).text = getString(
            R.string.quantity,
            product.nutritionFacts.protein.quantityPer100g.toString(),
            product.nutritionFacts.protein.unity
        )
        view.findViewById<TextView>(R.id.proteins_quantity_per_portion).text = getString(
            R.string.quantity,
            product.nutritionFacts.protein.quantityPerPortion.toString(),
            product.nutritionFacts.protein.unity
        )

        view.findViewById<TextView>(R.id.salt_quantity_per_100g).text = getString(
            R.string.quantity,
            product.nutritionFacts.salt.quantityPer100g.toString(),
            product.nutritionFacts.salt.unity
        )
        view.findViewById<TextView>(R.id.salt_quantity_per_portion).text = getString(
            R.string.quantity,
            product.nutritionFacts.salt.quantityPerPortion.toString(),
            product.nutritionFacts.salt.unity
        )

        view.findViewById<TextView>(R.id.sodium_quantity_per_100g).text = getString(
            R.string.quantity,
            product.nutritionFacts.sodium.quantityPer100g.toString(),
            product.nutritionFacts.sodium.unity
        )
        view.findViewById<TextView>(R.id.sodium_quantity_per_portion).text = getString(
            R.string.quantity,
            product.nutritionFacts.sodium.quantityPerPortion.toString(),
            product.nutritionFacts.sodium.unity
        )
    }
}