package com.azimmermannrosenthal.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.azimmermannrosenthal.myapplication.*
import com.azimmermannrosenthal.myapplication.adapters.ItemArtistAdapter
import com.azimmermannrosenthal.myapplication.adapters.ItemAlbumAdapter
import com.azimmermannrosenthal.myapplication.api.ApiClient
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.AlbumList
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.FoundedArtistList
import com.azimmermannrosenthal.myapplication.database.AlbumTable
import com.azimmermannrosenthal.myapplication.database.AppDatabase
import com.azimmermannrosenthal.myapplication.database.ArtistTable
import com.azimmermannrosenthal.myapplication.listeners.ItemClickListener
import com.azimmermannrosenthal.myapplication.objects.Album
import com.azimmermannrosenthal.myapplication.objects.Artist
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFavoritesFragment : Fragment() {
    private var albums = mutableListOf<Album>()
    private var artists = mutableListOf<Artist>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_home_favorites,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as AppCompatActivity

        activity.findViewById<TextView>(R.id.title).text = getString(R.string.tab_favorites)

        activity.findViewById<BottomNavigationView>(R.id.home_nav).visibility = View.VISIBLE

        var db = get_database_instance(activity)

        val artistDao = db.artistDao()
        val artistsTable: List<ArtistTable> = artistDao.getAll()
        var artistsIds: MutableList<String> = mutableListOf<String>()

        artistsIds = artistsTable.map { it.artistId } as MutableList<String>

        MainScope().launch(Dispatchers.Main) {
            artistsIds.forEach {
                artists.add(withContext(Dispatchers.Main) {
                    ApiClient.searchArtistByIdAsync(it)
                }.await().list[0])
            }
            setArtists(view, artists)
        }

        val albumDao = db.albumDao()
        val albumsTable: List<AlbumTable> = albumDao.getAll()
        var albumsIds: MutableList<String> = mutableListOf<String>()

        albumsIds = albumsTable.map { it.albumId } as MutableList<String>

        MainScope().launch(Dispatchers.Main) {
            albumsIds.forEach {
                albums.add(withContext(Dispatchers.Main) {
                    ApiClient.getAlbumsByIdAsync(it)
                }.await().list[0])
            }
            setAlbums(view, albums)
        }

    }

    private fun get_database_instance(activity: AppCompatActivity): AppDatabase {
        val db = Room.databaseBuilder(
            activity.applicationContext,
            AppDatabase::class.java, "musical-application"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
        return db
    }


    private fun setAlbums(
        view: View,
        albums: List<Album>
    ) {

        if (albums.isNotEmpty()) {
            view.findViewById<RecyclerView>(R.id.album_list).run {
                adapter = ItemAlbumAdapter(
                    albums,
                    listener = object : ItemClickListener {
                        override fun onItemClicked(position: Int) {
                            findNavController().navigate(
                                HomeFavoritesFragmentDirections.actionTabFavoritesToAlbumFragment(
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

    private fun setArtists(
        view: View,
        artists: List<Artist>
    ) {

        if (this.artists.isNotEmpty()) {
            view.findViewById<RecyclerView>(R.id.artist_list).run {
                adapter = ItemArtistAdapter(
                    artists,
                    listener = object : ItemClickListener {
                        override fun onItemClicked(position: Int) {
                            findNavController().navigate(
                                HomeFavoritesFragmentDirections.actionTabFavoritesToArtistFragment(
                                    artists[position]
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