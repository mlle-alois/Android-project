package com.azimmermannrosenthal.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()

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

        setTitle("Classements")

        executeCall()

        /*supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, ProductDetailsFragment())
            .commitAllowingStateLoss()*/
    }

    private fun executeCall() {
        MainScope().launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.getPostById(1)

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body() as Post
                    Log.d("INFO", content.toString())
                //do something
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Error Occurred: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}