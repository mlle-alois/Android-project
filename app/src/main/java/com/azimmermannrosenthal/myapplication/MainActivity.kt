package com.azimmermannrosenthal.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.azimmermannrosenthal.myapplication.fragments.ProductDetailsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //utilisation du dégradé en arrière plan de la toolbar
        supportActionBar?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.toolbar_effect
            )
        )

        setTitle("Mes produits")

        /*supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, ProductDetailsFragment())
            .commitAllowingStateLoss()*/
    }
}