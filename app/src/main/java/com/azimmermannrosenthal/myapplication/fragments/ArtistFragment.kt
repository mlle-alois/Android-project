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
                adapter = AlbumAdapter(
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

                //requireContext() correspond à this
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
                adapter = TrackOfAlbumAdapter(
                    tracks,
                    listener = object : ItemClickListener {
                        override fun onItemClicked(position: Int) {
                            Log.d("ITEM_CLICKED", "Position $position")
                            //ProductsListFragmentDirections généré automatiquement grâce au lien dans app-nav
                            /*findNavController().navigate(
                                ProductsListFragmentDirections.actionProductsListFragmentToProductDetailsFragment(
                                    products[position]
                                )
                            )*/
                        }
                    }
                )

                //requireContext() correspond à this
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    class AlbumAdapter(
        private val albums: List<Album>,
        val listener: ItemClickListener
    ) : RecyclerView.Adapter<AlbumCell>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumCell {
            return AlbumCell(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_album, parent, false)
            )
        }

        override fun onBindViewHolder(albumCell: AlbumCell, position: Int) {

            val album = albums[position]

            Picasso.get().load(album.strAlbumThumb)
                .into(albumCell.album_image)
            albumCell.album_title.text = album.strAlbum
            albumCell.album_date.text = album.intYearReleased

            albumCell.itemView.setOnClickListener {
                listener.onItemClicked(position)
            }
        }

        override fun getItemCount(): Int {
            return albums.size
        }

    }

    class AlbumCell(v: View) : RecyclerView.ViewHolder(v) {

        val album_image = v.findViewById<ImageView>(R.id.album_image)
        val album_title = v.findViewById<TextView>(R.id.album_title)
        val album_date = v.findViewById<TextView>(R.id.album_date)

    }

    class TrackOfAlbumAdapter(
        private val tracks: List<Track>,
        val listener: ItemClickListener
    ) : RecyclerView.Adapter<ListTrackCell>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTrackCell {
            return ListTrackCell(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_simple_track, parent, false)
            )
        }

        override fun onBindViewHolder(listTrackCell: ListTrackCell, position: Int) {

            val track = tracks[position]

            listTrackCell.track_number.text = (position + 1).toString()
            listTrackCell.track_title.text = track.strTrack

            listTrackCell.itemView.setOnClickListener {
                listener.onItemClicked(position)
            }
        }

        override fun getItemCount(): Int {
            return tracks.size
        }

    }

    class ListTrackCell(v: View) : RecyclerView.ViewHolder(v) {

        val track_number = v.findViewById<TextView>(R.id.track_number)
        val track_title = v.findViewById<TextView>(R.id.track_title)

    }
}