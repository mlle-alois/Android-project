package com.azimmermannrosenthal.myapplication.fragments

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.azimmermannrosenthal.myapplication.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFavoritesFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_home_favorites,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = Html.fromHtml("<font color=\"black\">" + getString(R.string.tab_favorites) + "</font>");

        (activity as AppCompatActivity).findViewById<BottomNavigationView>(R.id.home_nav).visibility = View.VISIBLE

        /*val product = ProductDetailsFragmentArgs.fromBundle(
            requireParentFragment().requireParentFragment().requireArguments()
        ).product*/
        val product = Product(
            "Petits pois et carottes",
            "Cassegrain",
            "3083680085304",
            Nutriscore.A,
            "static.openfoodfacts.org/images/products/308/368/008/5304/front_fr.7.400.jpg",
            "400g (280g net égoutté)",
            "France, Japon, Suisse",
            "Petits pois 66%, eau, garniture 2,8% (salade, oignon grelot), sucre, sel, arôme naturel",
            "Aucune",
            "Aucun",
            "234 kCal/part",
            NutritionFacts(
                NutritionFactsItem("kj", 293.0, 0.0),
                NutritionFactsItem("g", 0.8, 0.0),
                NutritionFactsItem("g", 0.1, 0.0),
                NutritionFactsItem("g", 8.4, 0.0),
                NutritionFactsItem("g", 5.2, 0.0),
                NutritionFactsItem("g", 5.2, 0.0),
                NutritionFactsItem("g", 4.8, 0.0),
                NutritionFactsItem("g", 0.75, 0.0),
                NutritionFactsItem("g", 0.295, 0.0)
            )
        )

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