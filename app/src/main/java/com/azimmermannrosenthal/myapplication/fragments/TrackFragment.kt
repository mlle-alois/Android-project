package com.azimmermannrosenthal.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.azimmermannrosenthal.myapplication.ItemClickListener
import com.azimmermannrosenthal.myapplication.R
import com.azimmermannrosenthal.myapplication.api.ApiClient
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.FoundedArtistList
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.TrackList
import com.azimmermannrosenthal.myapplication.database.AlbumTable
import com.azimmermannrosenthal.myapplication.database.AppDatabase
import com.azimmermannrosenthal.myapplication.objects.Album
import com.azimmermannrosenthal.myapplication.objects.Track
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

class TrackFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_track,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as AppCompatActivity
        activity.findViewById<TextView>(R.id.title).visibility = GONE
        activity.findViewById<BottomNavigationView>(R.id.home_nav).visibility = GONE

        val track: Track = TrackFragmentArgs.fromBundle(requireArguments()).track

        view.findViewById<TextView>(R.id.track_title).text = track.strTrack
        Picasso.get().load(track.strTrackThumb)
            .into(view.findViewById<ImageView>(R.id.track_background_image))
        Picasso.get().load(track.strTrackThumb).into(view.findViewById<ImageView>(R.id.track_image))

        /*view.findViewById<View>(R.id.album_artist).setOnClickListener {
            MainScope().launch(Dispatchers.Main) {
                try {
                    //TODO barre de chargement
                    //TODO rassembler ce code dans une classe spéciale API ?
                    val response = ApiClient.apiService.searchArtistByName(track.strArtist)

                    if (response.isSuccessful && response.body() != null) {
                        val content = response.body() as FoundedArtistList
                        findNavController().navigate(
                            AlbumFragmentDirections.actionAlbumFragmentToArtistFragment(content.artistList[0])
                        )
                    } else {
                        Log.d("ERROR", response.message())
                    }
                    //TODO permettre de relancer la requête en cas d'erreur
                } catch (e: Exception) {
                    Log.d("ERROR CATCH", e.message.toString())
                }
            }
        }*/

        view.findViewById<View>(R.id.return_button).setOnClickListener {
            findNavController().navigate(
                TrackFragmentDirections.actionTrackFragmentToTabRankings()
            )
        }
    }
}