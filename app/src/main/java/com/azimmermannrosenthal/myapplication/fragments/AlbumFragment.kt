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
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.azimmermannrosenthal.myapplication.R
import com.azimmermannrosenthal.myapplication.adapters.TrackAdapter
import com.azimmermannrosenthal.myapplication.api.ApiClient
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.FoundedArtistList
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.TrackList
import com.azimmermannrosenthal.myapplication.database.AlbumTable
import com.azimmermannrosenthal.myapplication.database.AppDatabase
import com.azimmermannrosenthal.myapplication.listeners.ItemClickListener
import com.azimmermannrosenthal.myapplication.objects.Album
import com.azimmermannrosenthal.myapplication.objects.Track
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AlbumFragment : Fragment() {

    private var tracks = listOf<Track>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_album,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as AppCompatActivity
        activity.findViewById<TextView>(R.id.title).visibility = GONE
        activity.findViewById<BottomNavigationView>(R.id.home_nav).visibility = GONE

        val album: Album = AlbumFragmentArgs.fromBundle(requireArguments()).album

        MainScope().launch(Dispatchers.Main) {
            tracks = withContext(Dispatchers.Main) {
                ApiClient.getTracksByAlbumIdAsync(album.idAlbum)
            }.await().list

            setMostLovedTracks(view, tracks)

            view.findViewById<TextView>(R.id.album_number_of_tracks).text = getString(R.string.songs, tracks.size.toString())
        }

        view.findViewById<TextView>(R.id.album_artist).text = album.strArtist
        view.findViewById<TextView>(R.id.row_item_title).text = album.strAlbum
        Picasso.get().load(album.strAlbumThumb)
            .into(view.findViewById<ImageView>(R.id.album_background_image))
        Picasso.get().load(album.strAlbumThumb)
            .into(view.findViewById<ImageView>(R.id.row_item_image))

        view.findViewById<TextView>(R.id.album_score).text = album.intScore
        view.findViewById<TextView>(R.id.album_votes).text =
            getString(R.string.votes, album.intScoreVotes)

        if(album.intScoreVotes == null) {
            view.findViewById<CardView>(R.id.votes_card).visibility = View.GONE
        }

        if(album.strDescriptionFR == null && album.strDescriptionEN == null && album.strDescriptionEN == null) {
            view.findViewById<TextView>(R.id.album_description).visibility = View.GONE
        }
        if (Locale.getDefault().displayLanguage == "français" && album.strDescriptionFR != null) {
            view.findViewById<TextView>(R.id.album_description).text = album.strDescriptionFR
        } else {
            if (album.strDescriptionEN == null) {
                view.findViewById<TextView>(R.id.album_description).text = album.strDescription
            } else {
                view.findViewById<TextView>(R.id.album_description).text = album.strDescriptionEN
            }
        }

        val db = Room.databaseBuilder(
            activity.applicationContext,
            AppDatabase::class.java, "musical-application"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

        val userDao = db.albumDao()
        var albums: List<AlbumTable> = userDao.getAll()

        val favoriteButtonOn: View = view.findViewById(R.id.favorite_button_on)

        if (isAlbumsListContainsId(albums, album.idAlbum)) {
            favoriteButtonOn.visibility = VISIBLE
        } else {
            favoriteButtonOn.visibility = GONE
        }

        view.findViewById<View>(R.id.favorite_button).setOnClickListener {
            if (isAlbumsListContainsId(albums, album.idAlbum)) {
                userDao.delete(findAlbumTableById(albums, album.idAlbum))
                favoriteButtonOn.visibility = GONE
                albums = userDao.getAll()
            } else {
                userDao.insert(AlbumTable(album.idAlbum, album.strAlbum, album.strArtist, album.strAlbumThumb))
                favoriteButtonOn.visibility = VISIBLE
                albums = userDao.getAll()
            }
        }

        view.findViewById<View>(R.id.album_artist).setOnClickListener {
            MainScope().launch(Dispatchers.Main) {
                findNavController().navigate(
                    AlbumFragmentDirections.actionAlbumFragmentToArtistFragment(
                        withContext(
                            Dispatchers.Main
                        ) {
                            ApiClient.searchArtistByNameAsync(album.strArtist)
                        }.await().list[0]
                    )
                )
            }
        }

        view.findViewById<View>(R.id.return_button).setOnClickListener {
            findNavController().navigate(
                AlbumFragmentDirections.actionAlbumFragmentToTabRankings()
            )
        }
    }

    private fun isAlbumsListContainsId(albums: List<AlbumTable>, idAlbum: String): Boolean {
        for (albumTable in albums) {
            if (albumTable.albumId == idAlbum) {
                return true
            }
        }
        return false
    }

    private fun findAlbumTableById(albums: List<AlbumTable>, idAlbum: String): AlbumTable {
        for (albumTable in albums) {
            if (albumTable.albumId == idAlbum) {
                return albumTable
            }
        }
        return albums[0]
    }

    private fun setMostLovedTracks(
        view: View,
        tracks: List<Track>
    ) {

        if (tracks.isNotEmpty()) {
            view.findViewById<RecyclerView>(R.id.track_list).run {
                adapter = TrackAdapter(
                    tracks,
                    listener = object : ItemClickListener {
                        override fun onItemClicked(position: Int) {
                            findNavController().navigate(
                                AlbumFragmentDirections.actionAlbumFragmentToTrackFragment(
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