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
import com.azimmermannrosenthal.myapplication.listeners.ItemClickListener
import com.azimmermannrosenthal.myapplication.R
import com.azimmermannrosenthal.myapplication.adapters.DetailledAlbumAdapter
import com.azimmermannrosenthal.myapplication.adapters.TrackOfArtistAdapter
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
import java.util.*

class ArtistFragment : Fragment() {

    private var albums = mutableListOf<Album>()
    private var tracks = mutableListOf<Track>()

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

        tracks = initTracks(view, artist.strArtist)
        albums = initAlbums(view, artist.idArtist, view.findViewById(R.id.albums))

        Picasso.get().load(artist.strArtistThumb)
            .into(view.findViewById<ImageView>(R.id.artist_image))
        view.findViewById<TextView>(R.id.artist_name).text = artist.strArtist
        view.findViewById<TextView>(R.id.artist_localisation_and_style).text =
            getString(R.string.artist_localisation_and_style, artist.strCountry, artist.strGenre)


        if (Locale.getDefault().displayLanguage == "français") {
            view.findViewById<TextView>(R.id.artist_description).text = artist.strBiographyFR
        } else {
            view.findViewById<TextView>(R.id.artist_description).text = artist.strBiographyEN
        }

        val db = Room.databaseBuilder(
            activity.applicationContext,
            AppDatabase::class.java, "musical-application"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

        val artistDao = db.artistDao()
        val artists: List<ArtistTable> = artistDao.getAll()
        val artistsIds: MutableList<String> = mutableListOf()
        for (artistTable: ArtistTable in artists) {
            artistsIds.add(artistTable.artistId)
        }

        val favoriteButtonOn: View = view.findViewById(R.id.favorite_button_on)
        if (artistsIds.contains(artist.idArtist)) {
            favoriteButtonOn.visibility = VISIBLE
        } else {
            favoriteButtonOn.visibility = GONE
        }

        view.findViewById<View>(R.id.favorite_button).setOnClickListener {
            if (artistsIds.contains(artist.idArtist)) {
                artistDao.delete(ArtistTable(artist.idArtist))
                favoriteButtonOn.visibility = GONE
            } else {
                artistDao.insert(ArtistTable(artist.idArtist))
                favoriteButtonOn.visibility = VISIBLE
            }
        }

        view.findViewById<View>(R.id.return_button).setOnClickListener {
            findNavController().navigate(
                ArtistFragmentDirections.actionArtistFragmentToTabRankings()
            )
        }
    }

    private fun initAlbums(
        view: View,
        artistId: String,
        numberOfAlbumsView: TextView
    ): MutableList<Album> {
        val albums: MutableList<Album> = arrayListOf()

        MainScope().launch(Dispatchers.Main) {
            try {
                //TODO barre de chargement
                //TODO rassembler ce code dans une classe spéciale API ?
                val response = ApiClient.apiService.getAlbumsByArtistId(artistId)

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body() as AlbumList
                    for (album: Album in content.albumList) {
                        albums.add(album)
                    }
                    numberOfAlbumsView.text = getString(R.string.number_of_albums, albums.size.toString())
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

    private fun initTracks(
        view: View,
        artistName: String
    ): MutableList<Track> {
        val tracks: MutableList<Track> = arrayListOf()

        MainScope().launch(Dispatchers.Main) {
            try {
                //TODO barre de chargement
                //TODO rassembler ce code dans une classe spéciale API ?
                val response = ApiClient.apiService.getTop10TracksByArtistName(artistName)

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body() as TrackList
                    for (track: Track in content.trackList) {
                        tracks.add(track)
                    }
                    setMostLovedTracks(view, tracks)
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

        if (tracks.isNotEmpty()) {
            view.findViewById<RecyclerView>(R.id.track_list).run {
                adapter = TrackOfArtistAdapter(
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