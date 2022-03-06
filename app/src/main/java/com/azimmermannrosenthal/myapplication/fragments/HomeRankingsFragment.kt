package com.azimmermannrosenthal.myapplication.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azimmermannrosenthal.myapplication.R
import com.azimmermannrosenthal.myapplication.adapters.HomeAlbumAdapter
import com.azimmermannrosenthal.myapplication.adapters.HomeTrackAdapter
import com.azimmermannrosenthal.myapplication.api.ApiClient
import com.azimmermannrosenthal.myapplication.listeners.AlbumClickListener
import com.azimmermannrosenthal.myapplication.listeners.TrackClickListener
import com.azimmermannrosenthal.myapplication.objects.Album
import com.azimmermannrosenthal.myapplication.objects.Track
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeRankingsFragment : Fragment() {

    private var tracks = listOf<Track>()
    private var albums = listOf<Album>()
    private var isOnTracks = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_home_rankings,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!isNetworkConnected())
            return

        MainScope().launch(Dispatchers.Main) {
            tracks = withContext(Dispatchers.Main) {
                ApiClient.getMostLovedTracksAsync()
            }.await().list

            albums = withContext(Dispatchers.Main) {
                ApiClient.getMostLovedAlbumsAsync()
            }.await().list

            setMostLovedTracks(view, tracks)

            view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
        }

        val title = (activity as AppCompatActivity).findViewById<TextView>(R.id.title)
        title.visibility = View.VISIBLE
        title.text = getString(R.string.tab_rankings)

        (activity as AppCompatActivity).findViewById<BottomNavigationView>(R.id.home_nav).visibility =
            View.VISIBLE

        // Switch entre titres et albums
        val tracksTextView: TextView = view.findViewById(R.id.tracks)
        val albumsTextView: TextView = view.findViewById(R.id.albmums)
        tracksTextView.setOnClickListener {
            setTracksOnScreen(tracksTextView, albumsTextView, view)
        }

        albumsTextView.setOnClickListener {
            setAlbumsOnScreen(tracksTextView, albumsTextView, view)
        }
    }

    private fun setTracksOnScreen(tracksTextView: TextView, albumsTextView: TextView, view: View) {
        isOnTracks = true
        tracksTextView.background =
            ContextCompat.getDrawable(view.context, R.drawable.home_textline)
        albumsTextView.background =
            ContextCompat.getDrawable(view.context, R.drawable.home_textline_disabled)
        setMostLovedTracks(view, tracks)
        tracksTextView.setTextColor(ContextCompat.getColor(view.context, R.color.black))
        albumsTextView.setTextColor(
            ContextCompat.getColor(
                view.context,
                R.color.light_grey
            )
        )
    }

    private fun setAlbumsOnScreen(tracksTextView: TextView, albumsTextView: TextView, view: View) {
        isOnTracks = false
        albumsTextView.background =
            ContextCompat.getDrawable(view.context, R.drawable.home_textline)
        tracksTextView.background =
            ContextCompat.getDrawable(view.context, R.drawable.home_textline_disabled)
        setMostLovedAlbums(view, albums)
        albumsTextView.setTextColor(ContextCompat.getColor(view.context, R.color.black))
        tracksTextView.setTextColor(
            ContextCompat.getColor(
                view.context,
                R.color.light_grey
            )
        )
    }

    private fun setMostLovedTracks(
        view: View,
        tracks: List<Track>
    ) {
        if (!tracks.isNullOrEmpty()) {
            view.findViewById<RecyclerView>(R.id.item_list).run {
                adapter = HomeTrackAdapter(
                    tracks,
                    listener = object : TrackClickListener {
                        override fun onTrackClicked(position: Int) {
                            findNavController().navigate(
                                HomeRankingsFragmentDirections.actionTabRankingsToTrackFragment(
                                    tracks[position]
                                )
                            )
                        }

                        override fun onArtistClicked(position: Int) {
                            navigateToArtist(tracks[position].strArtist)
                        }
                    }
                )
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun setMostLovedAlbums(
        view: View,
        albums: List<Album>
    ) {
        if (!albums.isNullOrEmpty()) {
            view.findViewById<RecyclerView>(R.id.item_list).run {
                adapter = HomeAlbumAdapter(
                    albums,
                    listener = object : AlbumClickListener {
                        override fun onAlbumClicked(position: Int) {
                            findNavController().navigate(
                                HomeRankingsFragmentDirections.actionTabRankingsToAlbumFragment(
                                    albums[position]
                                )
                            )
                        }

                        override fun onArtistClicked(position: Int) {
                            navigateToArtist(albums[position].strArtist)
                        }
                    }
                )
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun navigateToArtist(artistName: String) {
        MainScope().launch(Dispatchers.Main) {
            findNavController().navigate(
                HomeRankingsFragmentDirections.actionTabRankingsToArtistFragment(
                    withContext(Dispatchers.Main) {
                        ApiClient.searchArtistByNameAsync(artistName)
                    }.await().list[0]
                )
            )
        }
    }
    private fun isNetworkConnected(): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }
}