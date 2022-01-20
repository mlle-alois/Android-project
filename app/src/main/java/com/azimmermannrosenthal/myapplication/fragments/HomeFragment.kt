package com.azimmermannrosenthal.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.azimmermannrosenthal.myapplication.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_home,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //menu à la main
        /*view.product_details_bottom_nav.setOnNavigationItemSelectedListener {
            childFragmentManager.beginTransaction()
                .replace(
                    R.id.product_details_nav_host, when (it.itemId) {
                        R.id.tab_card -> ProductDetailsSummaryFragment()
                        R.id.tab_nutrition -> ProductDetailsNutritionFragment()
                        R.id.tab_array -> ProductDetailsNutritionalValuesFragment()
                        else -> throw Exception("Je panique")
                    }
                )
                .commitAllowingStateLoss()
            true
        }*/

        //alternative avec le graphe de navigation (où on associe des fragments au menu par le nom)
        val navHost = childFragmentManager.findFragmentById(R.id.home_nav_host) as NavHostFragment
        NavigationUI.setupWithNavController(home_nav, navHost.navController)

        //récupération de l'argument product
        //val product = ProductDetailsFragmentArgs.fromBundle(requireArguments()).product
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

    }
}