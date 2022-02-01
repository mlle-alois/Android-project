package com.azimmermannrosenthal.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.azimmermannrosenthal.myapplication.api.ApiClient
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.AlbumList
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.FoundedArtistList
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.LovedAlbumList
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.TrackList
import com.azimmermannrosenthal.myapplication.database.AlbumTable
import com.azimmermannrosenthal.myapplication.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        executeCall()
    }

    private fun executeCall() {

        // REGION RESEARCH ARTIST
        MainScope().launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.searchArtistById("112024")

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body() as FoundedArtistList
                    /*for(artist: Artist in content.artistList) {
                        Log.d("INFO", artist.toString())
                    }*/
                    //do something
                } else {
                    Log.d("ERROR", response.message())
                }

            } catch (e: Exception) {
                Log.d("ERROR CATCH", e.message.toString())
            }
        }
        // ENDREGION

        // REGION GET TOP 10 TRACKS BY ARTIST NAME
        MainScope().launch(Dispatchers.Main) {
            try {
                val response = ApiClient.apiService.getTop10TracksByArtistName("Céline Dion")

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body() as TrackList
                    /*for(track: Track in content.trackList) {
                        Log.d("INFO", track.toString())
                    }*/
                    //do something
                } else {
                    Log.d("ERROR", response.message())
                }

            } catch (e: Exception) {
                Log.d("ERROR CATCH", e.message.toString())
            }
        }
        // ENDREGION
    }
}