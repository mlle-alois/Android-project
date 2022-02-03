package com.azimmermannrosenthal.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.azimmermannrosenthal.myapplication.*
import com.azimmermannrosenthal.myapplication.adapters.ArtistAdapter
import com.azimmermannrosenthal.myapplication.adapters.DetailledAlbumAdapter
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
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

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
        val artists: List<ArtistTable> = artistDao.getAll()
        var artistsIds: MutableList<String> = mutableListOf<String>()

       artistsIds = artists.map { it.artistId  } as MutableList<String>
        artistsIds.forEach { initArtists(view,it).also { this.artists = it } }


        val albumDao = db.albumDao()
        val albums: List<AlbumTable> = albumDao.getAll()
        var albumsIds: MutableList<String> = mutableListOf<String>()

        albumsIds = albums.map { it.albumId } as MutableList<String>
        albumsIds.forEach { initAlbums(view,it).also { this.albums = it } }

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
                adapter = DetailledAlbumAdapter(
                    albums,
                    listener = object : ItemClickListener {
                        override fun onItemClicked(position: Int) {
                            findNavController().navigate(
                                HomeFavoritesFragmentDirections.actionTabFavoritesToAlbumFragment(albums[position])
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
                adapter = ArtistAdapter(
                    artists,
                    listener = object : ItemClickListener {
                        override fun onItemClicked(position: Int) {
                            findNavController().navigate(
                                HomeFavoritesFragmentDirections.actionTabFavoritesToArtistFragment(artists[position])
                            )
                        }
                    }
                )
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun initArtists(
        view: View,
        artistId: String
    ): MutableList<Artist> {
        val artists: MutableList<Artist> = arrayListOf()

        MainScope().launch(Dispatchers.Main) {
            try {
                Log.d("Input : ", artistId)
                val response = ApiClient.apiService.searchArtistById(artistId)
                Log.d("Response :", response.toString())
                if (response.isSuccessful && response.body() != null) {
                    val content = response.body() as FoundedArtistList
                    for (artist: Artist in content.artistList) {
                        this@HomeFavoritesFragment.artists.add(artist)
                    }
                    setArtists(view, artists)
                } else {
                    Log.d("ERROR", response.message())
                }
                //TODO permettre de relancer la requête en cas d'erreur

            } catch (e: Exception) {
                Log.d("ERROR CATCH", e.message.toString())
            }
        }
        Log.d("artistes : ", artists.toString())
        return artists
    }

    private fun initAlbums(
        view: View,
        albumId: String,
    ): MutableList<Album> {
        val albums: MutableList<Album> = arrayListOf()

        MainScope().launch(Dispatchers.Main) {
            try {
                //TODO barre de chargement
                //TODO rassembler ce code dans une classe spéciale API ?
                val response = ApiClient.apiService.getAlbumsById(albumId)


                if (response.isSuccessful && response.body() != null) {
                    val content = response.body() as AlbumList
                    for (album: Album in content.albumList) {
                        this@HomeFavoritesFragment.albums.add(album)
                    }
                    setAlbums(view, albums)
                } else {
                    Log.d("ERROR", response.message())
                }
                //TODO permettre de relancer la requête en cas d'erreur

            } catch (e: Exception) {
                Log.d("ERROR CATCH", e.message.toString())
            }
        }
        return albums
    }


}