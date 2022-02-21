package com.azimmermannrosenthal.myapplication.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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

        val db = getDatabaseInstance(activity)

        val artistDao = db.artistDao()
        val artistsTable: List<ArtistTable> = artistDao.getAll()

        val albumDao = db.albumDao()
        val albumsTable: List<AlbumTable> = albumDao.getAll()

        if (isInternetAvailable(view.context)) {
            val artistsIds = artistsTable.map { it.artistId } as MutableList<String>

            MainScope().launch(Dispatchers.Main) {
                artistsIds.forEach {
                    artists.add(withContext(Dispatchers.Main) {
                        ApiClient.searchArtistByIdAsync(it)
                    }.await().list[0])
                }

                setArtists(view, artists)
                view.findViewById<ProgressBar>(R.id.progressBarArtists).visibility = View.GONE

                if (artists.isEmpty()) {
                    view.findViewById<TextView>(R.id.artists).visibility = View.GONE
                    //TODO message de vide
                } else {
                    view.findViewById<TextView>(R.id.artists).visibility = View.VISIBLE
                }
            }

            val albumsIds = albumsTable.map { it.albumId } as MutableList<String>

            MainScope().launch(Dispatchers.Main) {
                albumsIds.forEach {
                    albums.add(withContext(Dispatchers.Main) {
                        ApiClient.getAlbumsByIdAsync(it)
                    }.await().list[0])
                }
                setAlbums(view, albums)
                view.findViewById<ProgressBar>(R.id.progressBarAlbums).visibility = View.GONE

                if (albums.isEmpty()) {
                    view.findViewById<TextView>(R.id.albums).visibility = View.GONE
                    //TODO message de vide
                } else {
                    view.findViewById<TextView>(R.id.albums).visibility = View.VISIBLE
                }
            }
        } else {
            albums = albumsTable.map {
                Album(
                    it.albumId,
                    "",
                    it.strAlbum,
                    it.strArtist,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    it.strAlbumThumb
                )
            } as MutableList<Album>
            setAlbums(view, albums)
            artists = artistsTable.map {
                Artist(
                    it.artistId,
                    it.strArtist,
                    "",
                    "",
                    "",
                    "",
                    "",
                    it.strArtistThumb,
                    ""
                )
            } as MutableList<Artist>
            setArtists(view, artists)
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }
        return result
    }

    private fun getDatabaseInstance(activity: AppCompatActivity): AppDatabase {
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