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
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.TrackList
import com.azimmermannrosenthal.myapplication.objects.Album
import com.azimmermannrosenthal.myapplication.objects.Track
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AlbumFragment : Fragment() {

    private var tracks = mutableListOf<Track>()

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

        //cacher la navigation bar
        (activity as AppCompatActivity).supportActionBar?.hide()
        //cacher le menu
        (activity as AppCompatActivity).findViewById<BottomNavigationView>(R.id.home_nav).visibility = View.GONE

        val album: Album = AlbumFragmentArgs.fromBundle(requireArguments()).album

        tracks = initTracks(view, album.idAlbum, view.findViewById(R.id.album_number_of_tracks))

        view.findViewById<TextView>(R.id.album_artist).text = album.strArtist
        view.findViewById<TextView>(R.id.album_title).text = album.strAlbum
        Picasso.get().load(album.strAlbumThumb)
            .into(view.findViewById<ImageView>(R.id.album_background_image))
        Picasso.get().load(album.strAlbumThumb).into(view.findViewById<ImageView>(R.id.album_image))
        //TODO mise en favoris
        view.findViewById<TextView>(R.id.album_score).text = album.intScore
        view.findViewById<TextView>(R.id.album_votes).text =
            getString(R.string.votes, album.intScoreVotes)
        //TODO appliquer la description selon la bonne langue
        view.findViewById<TextView>(R.id.album_description).text = album.strDescriptionFR

        // Fonctionnement bouton retour
        view.findViewById<View>(R.id.return_button).setOnClickListener{
            findNavController().navigate(
            AlbumFragmentDirections.actionAlbumFragmentToTabRankings())
        }
    }

    fun initTracks(
        view: View,
        albumId: String,
        numberOfTracksView: TextView
    ): MutableList<Track> {
        val tracks: MutableList<Track> = arrayListOf()

        MainScope().launch(Dispatchers.Main) {
            try {
                //TODO barre de chargement
                //TODO rassembler ce code dans une classe spéciale API ?
                val response = ApiClient.apiService.getTracksByAlbumId(albumId)

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body() as TrackList
                    for (track: Track in content.trackList) {
                        tracks.add(track)
                    }
                    numberOfTracksView.text = getString(R.string.songs, tracks.size.toString())
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

    fun setMostLovedTracks(
        view: View,
        tracks: List<Track>
    ) {

        if (tracks.isNotEmpty()) {
            view.findViewById<RecyclerView>(R.id.track_list).run {
                adapter = TrackOfAlbumAdapter(
                    tracks,
                    listener = object : TrackClickListener {
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

    class TrackOfAlbumAdapter(
        private val tracks: List<Track>,
        val listener: TrackClickListener
    ) : RecyclerView.Adapter<ListTrackCell>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTrackCell {
            return ListTrackCell(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.album_track_list, parent, false)
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

    interface TrackClickListener {
        fun onItemClicked(position: Int);
    }
}