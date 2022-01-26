package com.azimmermannrosenthal.myapplication.fragments

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azimmermannrosenthal.myapplication.R
import com.azimmermannrosenthal.myapplication.api.ApiClient
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.LovedAlbumList
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.LovedTrackList
import com.azimmermannrosenthal.myapplication.objects.Album
import com.azimmermannrosenthal.myapplication.objects.Track
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class HomeRankingsFragment : Fragment() {

    private var tracks = mutableListOf<Track>()
    private var albums = mutableListOf<Album>()

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

        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title =
            Html.fromHtml("<font color=\"black\">" + getString(R.string.tab_rankings) + "</font>")

        (activity as AppCompatActivity).findViewById<BottomNavigationView>(R.id.home_nav).visibility = View.VISIBLE

        // Switch entre titres et albums
        val tracksTextView: TextView = view.findViewById(R.id.tracks)
        val albumsTextView: TextView = view.findViewById(R.id.albmums)
        tracksTextView.setOnClickListener {
            it.background = ContextCompat.getDrawable(view.context, R.drawable.home_textline)
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

        albumsTextView.setOnClickListener {
            it.background = ContextCompat.getDrawable(view.context, R.drawable.home_textline)
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
                    for (track: Track in content.trackList) {
                        if (tracks.size >= 8) {
                            break
                        }
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

    private fun initAlbums(): MutableList<Album> {
        val albums: MutableList<Album> = arrayListOf()
        MainScope().launch(Dispatchers.Main) {
            try {
                //TODO barre de chargement
                val response = ApiClient.apiService.getMostLovedAlbums()

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body() as LovedAlbumList
                    for (album: Album in content.albumList) {
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
                adapter = TrackAdapter(
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

    private fun setMostLovedAlbums(
        view: View,
        albums: List<Album>
    ) {
        if (albums.isNotEmpty()) {
            view.findViewById<RecyclerView>(R.id.item_list).run {
                adapter = AlbumAdapter(
                    albums,
                    listener = object : ItemClickListener {
                        override fun onItemClicked(position: Int) {
                            Log.d("ITEM_CLICKED", "Position $position")
                            findNavController().navigate(
                                HomeRankingsFragmentDirections.actionTabRankingsToAlbumFragment(
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

    class TrackAdapter(
        private val tracks: List<Track>,
        val listener: ItemClickListener
    ) : RecyclerView.Adapter<ListItemCell>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemCell {
            return ListItemCell(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.home_list, parent, false)
            )
        }

        override fun onBindViewHolder(listItemCell: ListItemCell, position: Int) {

            val track = tracks[position]

            listItemCell.item_number.text = (position + 1).toString()
            Picasso.get().load(track.strTrackThumb).into(listItemCell.item_image)
            listItemCell.item_title.text = track.strTrack
            listItemCell.item_artist.text = track.strArtist

            listItemCell.itemView.setOnClickListener {
                listener.onItemClicked(position)
            }
        }

        override fun getItemCount(): Int {
            return tracks.size
        }

    }

    class ListItemCell(v: View) : RecyclerView.ViewHolder(v) {

        val item_number: TextView = v.findViewById(R.id.item_number)
        val item_image: ImageView = v.findViewById(R.id.item_image)
        val item_title: TextView = v.findViewById(R.id.item_title)
        val item_artist: TextView = v.findViewById(R.id.item_artist)

    }

    class AlbumAdapter(
        private val albums: List<Album>,
        val listener: ItemClickListener
    ) : RecyclerView.Adapter<ListItemCell>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemCell {
            return ListItemCell(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.home_list, parent, false)
            )
        }

        override fun onBindViewHolder(listItemCell: ListItemCell, position: Int) {

            val track = albums[position]

            listItemCell.item_number.text = (position + 1).toString()
            Picasso.get().load(track.strAlbumThumb).into(listItemCell.item_image)
            listItemCell.item_title.text = track.strAlbum
            listItemCell.item_artist.text = track.strArtist

            listItemCell.itemView.setOnClickListener {
                listener.onItemClicked(position)
            }
        }

        override fun getItemCount(): Int {
            return albums.size
        }

    }

    interface ItemClickListener {
        fun onItemClicked(position: Int)
    }
}