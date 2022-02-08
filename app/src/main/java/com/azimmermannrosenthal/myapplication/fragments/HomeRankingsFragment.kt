package com.azimmermannrosenthal.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azimmermannrosenthal.myapplication.listeners.AlbumClickListener
import com.azimmermannrosenthal.myapplication.R
import com.azimmermannrosenthal.myapplication.adapters.HomeAlbumAdapter
import com.azimmermannrosenthal.myapplication.adapters.HomeTrackAdapter
import com.azimmermannrosenthal.myapplication.listeners.TrackClickListener
import com.azimmermannrosenthal.myapplication.api.ApiClient
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.FoundedArtistList
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.LovedAlbumList
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.LovedTrackList
import com.azimmermannrosenthal.myapplication.objects.Album
import com.azimmermannrosenthal.myapplication.objects.Track
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class HomeRankingsFragment : Fragment() {

    private var tracks = mutableListOf<Track>()
    private var albums = mutableListOf<Album>()
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

        tracks = initTracks(view)
        albums = initAlbums()

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

    private fun initTracks(view: View): MutableList<Track> {
        val tracks: MutableList<Track> = arrayListOf()

        MainScope().launch(Dispatchers.Main) {
            try {
                //TODO barre de chargement
                //TODO rassembler ce code dans une classe spéciale API ?
                val response = ApiClient.apiService.getMostLovedTracks()

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body() as LovedTrackList
                    for (track: Track in content.list) {
                        if (tracks.size >= 8) {
                            break
                        }
                        tracks.add(track)
                    }
                    if (isOnTracks) {
                        setMostLovedTracks(view, tracks)
                    }
                } else {
                    Log.d("ERROR", response.message())
                }
                //TODO permettre de relancer la requête en cas d'erreur
            } catch (e: Exception) {
                Log.d("ERROR CATCH", e.message.toString())
            }
        }
        return tracks
    }

    private fun initAlbums(): MutableList<Album> {
        val albums: MutableList<Album> = arrayListOf()
        MainScope().launch(Dispatchers.Main) {
            try {
                //TODO barre de chargement
                val response = ApiClient.apiService.getMostLovedAlbums()

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body() as LovedAlbumList
                    for (album: Album in content.list) {
                        if (albums.size >= 8) {
                            break
                        }
                        albums.add(album)
                    }
                } else {
                    Log.d("ERROR", response.message())
                }

            } catch (e: Exception) {
                Log.d("ERROR CATCH", e.message.toString())
            }
        }
        return albums
    }

    private fun setMostLovedTracks(
        view: View,
        tracks: List<Track>
    ) {
        if (tracks.isNotEmpty()) {
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
        if (albums.isNotEmpty()) {
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
            try {
                val response = ApiClient.apiService.searchArtistByName(artistName)

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body() as FoundedArtistList
                    findNavController().navigate(
                        HomeRankingsFragmentDirections.actionTabRankingsToArtistFragment(
                            content.list[0]
                        )
                    )
                } else {
                    Log.d("ERROR", response.message())
                }

            } catch (e: Exception) {
                Log.d("ERROR CATCH", e.message.toString())
            }
        }
    }
}