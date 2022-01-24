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

        val navHost = childFragmentManager.findFragmentById(R.id.home_nav_host) as NavHostFragment
        NavigationUI.setupWithNavController(home_nav, navHost.navController)

        //récupération de l'argument product
        //val product = ProductDetailsFragmentArgs.fromBundle(requireArguments()).product

    }
}