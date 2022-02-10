package com.azimmermannrosenthal.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.azimmermannrosenthal.myapplication.listeners.ItemClickListener
import com.azimmermannrosenthal.myapplication.R
import com.azimmermannrosenthal.myapplication.adapters.ItemAlbumAdapter
import com.azimmermannrosenthal.myapplication.adapters.TrackAdapter
import com.azimmermannrosenthal.myapplication.api.ApiClient
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.AlbumList
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.TrackList
import com.azimmermannrosenthal.myapplication.database.AppDatabase
import com.azimmermannrosenthal.myapplication.database.ArtistTable
import com.azimmermannrosenthal.myapplication.objects.Album
import com.azimmermannrosenthal.myapplication.objects.Artist
import com.azimmermannrosenthal.myapplication.objects.Track
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ArtistFragment : Fragment() {

    private var albums = listOf<Album>()
    private var tracks = listOf<Track>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_artist,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as AppCompatActivity
        activity.findViewById<TextView>(R.id.title).visibility = GONE
        activity.findViewById<BottomNavigationView>(R.id.home_nav).visibility = GONE

        val artist: Artist = ArtistFragmentArgs.fromBundle(requireArguments()).artist

        val albumsTextView: TextView = view.findViewById(R.id.albums)
        MainScope().launch(Dispatchers.Main) {
            tracks = withContext(Dispatchers.Main) {
                ApiClient.getTop10TracksByArtistNameAsync(artist.strArtist)
            }.await().list

            albums = withContext(Dispatchers.Main) {
                ApiClient.getAlbumsByArtistIdAsync(artist.idArtist)
            }.await().list

            setMostLovedTracks(view, tracks)
            setAlbums(view, albums)

            albumsTextView.text = getString(R.string.number_of_albums, albums.size.toString())
            if(albums == null) {
                albumsTextView.visibility = View.GONE
            }
            if(tracks == null) {
                view.findViewById<TextView>(R.id.most_popular_tracks).visibility = View.GONE
            }
        }

        Picasso.get().load(artist.strArtistThumb)
            .into(view.findViewById<ImageView>(R.id.artist_image))
        view.findViewById<TextView>(R.id.artist_name).text = artist.strArtist
        view.findViewById<TextView>(R.id.artist_localisation_and_style).text =
            getString(R.string.artist_localisation_and_style, artist.strCountry, artist.strGenre)

        val artistDescriptionTextView: TextView = view.findViewById(R.id.artist_description)
        if(artist.strBiographyFR == null && artist.strBiographyEN == null) {
            view.findViewById<ScrollView>(R.id.artist_description_scroll_view).visibility = GONE
        }
        if (Locale.getDefault().displayLanguage == "français") {
            artistDescriptionTextView.text = artist.strBiographyFR
        } else {
            artistDescriptionTextView.text = artist.strBiographyEN
        }

        val db = Room.databaseBuilder(
            activity.applicationContext,
            AppDatabase::class.java, "musical-application"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

        val artistDao = db.artistDao()
        var artists: List<ArtistTable> = artistDao.getAll()

        val favoriteButtonOn: View = view.findViewById(R.id.favorite_button_on)
        if (isArtistListContainsId(artists, artist.idArtist)) {
            favoriteButtonOn.visibility = VISIBLE
        } else {
            favoriteButtonOn.visibility = GONE
        }

        view.findViewById<View>(R.id.favorite_button).setOnClickListener {
            if (isArtistListContainsId(artists, artist.idArtist)) {
                artistDao.delete(findArtistTableById(artists, artist.idArtist))
                favoriteButtonOn.visibility = GONE
                artists = artistDao.getAll()
            } else {
                artistDao.insert(ArtistTable(artist.idArtist, artist.strArtist, artist.strArtistThumb))
                favoriteButtonOn.visibility = VISIBLE
                artists = artistDao.getAll()
            }
        }

        view.findViewById<View>(R.id.return_button).setOnClickListener {
            findNavController().navigate(
                ArtistFragmentDirections.actionArtistFragmentToTabRankings()
            )
        }
    }

    private fun isArtistListContainsId(artists: List<ArtistTable>, idArtist: String): Boolean {
        for(artistTable in artists) {
            if(artistTable.artistId == idArtist) {
                return true
            }
        }
        return false
    }

    private fun findArtistTableById(artists: List<ArtistTable>, idArtist: String): ArtistTable {
        for(artistTable in artists) {
            if(artistTable.artistId == idArtist) {
                return artistTable
            }
        }
        return artists[0]
    }

    private fun setAlbums(
        view: View,
        albums: List<Album>
    ) {

        if (albums != null && albums.isNotEmpty()) {
            view.findViewById<RecyclerView>(R.id.album_list).run {
                adapter = ItemAlbumAdapter(
                    albums,
                    listener = object : ItemClickListener {
                        override fun onItemClicked(position: Int) {
                            findNavController().navigate(
                                ArtistFragmentDirections.actionArtistFragmentToAlbumFragment(
                                    albums[position]
                                )
                            )
                        }
                    }
                )
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun setMostLovedTracks(
        view: View,
        tracks: List<Track>
    ) {

        if (tracks != null && tracks.isNotEmpty()) {
            view.findViewById<RecyclerView>(R.id.track_list).run {
                adapter = TrackAdapter(
                    tracks,
                    listener = object : ItemClickListener {
                        override fun onItemClicked(position: Int) {
                            findNavController().navigate(
                                ArtistFragmentDirections.actionArtistFragmentToTrackFragment(
                                    tracks[position]
                                )
                            )
                        }
                    }
                )
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }
}