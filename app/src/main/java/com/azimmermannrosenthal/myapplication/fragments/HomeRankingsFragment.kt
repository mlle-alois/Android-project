package com.azimmermannrosenthal.myapplication.fragments

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azimmermannrosenthal.myapplication.*
import com.azimmermannrosenthal.myapplication.api.ApiClient
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.LovedAlbumList
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.LovedTrackList
import com.azimmermannrosenthal.myapplication.objects.Album
import com.azimmermannrosenthal.myapplication.objects.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import com.squareup.picasso.Picasso

class HomeRankingsFragment : Fragment() {

    private var tracks = mutableListOf<Track>()
    private var albums = mutableListOf<Album>()

    // Unique id for loader
    private val LDR_BASIC_ID = 1

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

        (activity as AppCompatActivity).supportActionBar?.title =
            Html.fromHtml("<font color=\"black\">" + getString(R.string.tab_rankings) + "</font>")

        // Switch entre titres et albums
        val tracksTextView: TextView = view.findViewById(R.id.tracks)
        tracksTextView.setOnClickListener(View.OnClickListener {
            it.background = ContextCompat.getDrawable(view.context, R.drawable.home_textline)
            view.findViewById<TextView>(R.id.albmums).background =
                ContextCompat.getDrawable(view.context, R.drawable.home_textline_disabled)
            setMostLovedTracks(view, tracks)
            view.findViewById<TextView>(R.id.tracks)
                .setTextColor(ContextCompat.getColor(view.context, R.color.black))
            view.findViewById<TextView>(R.id.albmums)
                .setTextColor(ContextCompat.getColor(view.context, R.color.home_light_grey))
        })

        val albumsTextView: TextView = view.findViewById(R.id.albmums)
        albumsTextView.setOnClickListener(View.OnClickListener {
            it.background = ContextCompat.getDrawable(view.context, R.drawable.home_textline)
            view.findViewById<TextView>(R.id.tracks).background =
                ContextCompat.getDrawable(view.context, R.drawable.home_textline_disabled)
            setMostLovedAlbums(view, albums)
            view.findViewById<TextView>(R.id.albmums)
                .setTextColor(ContextCompat.getColor(view.context, R.color.black))
            view.findViewById<TextView>(R.id.tracks)
                .setTextColor(ContextCompat.getColor(view.context, R.color.home_light_grey))
        })
    }

    fun initTracks(
        view: View
    ): MutableList<Track> {
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
        return tracks;
    }

    fun initAlbums(): MutableList<Album> {
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
        return albums;
    }

    fun setMostLovedTracks(
        view: View,
        tracks: List<Track>
    ) {

        if (tracks.isNotEmpty()) {
            view.findViewById<RecyclerView>(R.id.track_list).run {
                adapter = TrackAdapter(
                    tracks,
                    //findViewById<RecyclerView>(R.id.track_list).context,
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

    fun setMostLovedAlbums(
        view: View,
        albums: List<Album>
    ) {
        if (albums.isNotEmpty()) {
            view.findViewById<RecyclerView>(R.id.track_list).run {
                adapter = AlbumAdapter(
                    albums,
                    //findViewById<RecyclerView>(R.id.track_list).context,
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

    class TrackAdapter(
        val tracks: List<Track>,
        val listener: ItemClickListener
    ) : RecyclerView.Adapter<ListItemCell>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemCell {
            return ListItemCell(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.home_list, parent, false)
            )
        }

        override fun onBindViewHolder(listItemCell: ListItemCell, position: Int) {

            val track = tracks.get(position)

            listItemCell.item_number.text = (position + 1).toString()
            Picasso.get().load(track.strTrackThumb).into(listItemCell.item_image)
            listItemCell.item_title.text = track.strTrack
            listItemCell.item_artist.text = track.strArtist

            /*listTrackCell.itemView.setOnClickListener {
                listener.onItemClicked(position)
            }*/
        }

        override fun getItemCount(): Int {
            return tracks.size
        }

    }

    class ListItemCell(v: View) : RecyclerView.ViewHolder(v) {

        val item_number = v.findViewById<TextView>(R.id.item_number)
        val item_image = v.findViewById<ImageView>(R.id.item_image)
        val item_title = v.findViewById<TextView>(R.id.item_title)
        val item_artist = v.findViewById<TextView>(R.id.item_artist)

    }

    class AlbumAdapter(
        val albums: List<Album>,
        val listener: ItemClickListener
    ) : RecyclerView.Adapter<ListItemCell>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemCell {
            return ListItemCell(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.home_list, parent, false)
            )
        }

        override fun onBindViewHolder(listItemCell: ListItemCell, position: Int) {

            val track = albums.get(position)

            listItemCell.item_number.text = (position + 1).toString()
            Picasso.get().load(track.strAlbumThumb).into(listItemCell.item_image)
            listItemCell.item_title.text = track.strAlbum
            listItemCell.item_artist.text = track.strArtist

            /*listTrackCell.itemView.setOnClickListener {
                listener.onItemClicked(position)
            }*/
        }

        override fun getItemCount(): Int {
            return albums.size
        }

    }

    interface ItemClickListener {
        fun onItemClicked(position: Int);
    }
}