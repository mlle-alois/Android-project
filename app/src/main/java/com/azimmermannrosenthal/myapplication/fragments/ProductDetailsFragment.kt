package com.azimmermannrosenthal.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.azimmermannrosenthal.myapplication.R
import kotlinx.android.synthetic.main.fragment_product_details.*
import kotlinx.android.synthetic.main.fragment_product_details.view.*

class ProductDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_product_details,
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
        val navHost = childFragmentManager.findFragmentById(R.id.product_details_nav_host) as NavHostFragment
        NavigationUI.setupWithNavController(product_details_bottom_nav, navHost.navController)

        //récupération de l'argument product
        val product = ProductDetailsFragmentArgs.fromBundle(requireArguments()).product

    }
}