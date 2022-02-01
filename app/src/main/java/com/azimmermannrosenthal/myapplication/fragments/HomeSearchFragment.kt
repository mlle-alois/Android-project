package com.azimmermannrosenthal.myapplication.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azimmermannrosenthal.myapplication.ItemClickListener
import com.azimmermannrosenthal.myapplication.R
import com.azimmermannrosenthal.myapplication.api.ApiClient
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.AlbumList
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.FoundedArtistList
import com.azimmermannrosenthal.myapplication.objects.Album
import com.azimmermannrosenthal.myapplication.objects.Artist
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class HomeSearchFragment : Fragment() {

    private var albums = mutableListOf<Album>()
    private var artists = mutableListOf<Artist>()
    private var allArtist = mutableListOf<Artist>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_home_search,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).findViewById<TextView>(R.id.title).text =
            getString(R.string.tab_research)

        (activity as AppCompatActivity).findViewById<BottomNavigationView>(R.id.home_nav).visibility =
            View.VISIBLE

        clearSearchListener(view)

        SetOnTextChangeListener(view)

    }

    private fun SetOnTextChangeListener(view: View) {
        val editText = view.findViewById<View>(R.id.plain_text_input) as EditText
        editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                if (s != "") {
                    //do your work here
                    artists = initArtists(view, s.toString() + "%")
                    Log.d("artists : ", allArtist.toString())

                    if (allArtist.isNotEmpty()) {
                        Log.d("artist id : ", allArtist.takeLast(1)[0].idArtist)
                        albums =
                            initAlbums(view, allArtist.takeLast(1)[0].idArtist)
                    }

                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun initAlbums(
        view: View,
        artistId: String,
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
                        this@HomeSearchFragment.albums.add(album)
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

    private fun initArtists(
        view: View,
        artistName: String
    ): MutableList<Artist> {
        val artists: MutableList<Artist> = arrayListOf()

        MainScope().launch(Dispatchers.Main) {
            try {
                //TODO barre de chargement
                //TODO rassembler ce code dans une classe spéciale API ?
                Log.d("Input : ", artistName)
                val response = ApiClient.apiService.searchArtistByName(artistName)
                Log.d("Response :", response.toString())
                if (response.isSuccessful && response.body() != null) {
                    val content = response.body() as FoundedArtistList
                    for (artist: Artist in content.artistList) {
                        this@HomeSearchFragment.artists.add(artist)
                        this@HomeSearchFragment.allArtist.add(artist)

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

    private fun clearSearchListener(view: View) {
        val editText = view.findViewById<EditText>(R.id.plain_text_input)
        val button = view.findViewById<Button>(R.id.calc_clear_txt_Prise)
        button.setOnClickListener {
            editText.text.clear()
        }
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
                                HomeSearchFragmentDirections.actionTabSearchToAlbumFragment(albums[position])
                            )
                        }
                    }
                )

                //requireContext() correspond à this
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
                                HomeSearchFragmentDirections.actionTabSearchToArtistFragment(artists[position])
                            )
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
                    .inflate(R.layout.list_detailled_item, parent, false)
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

        val album_image = v.findViewById<ImageView>(R.id.row_item_image)
        val album_title = v.findViewById<TextView>(R.id.row_item_title)
        val album_date = v.findViewById<TextView>(R.id.row_item_date)

    }

    class ArtistAdapter(
        private val artist_list: List<Artist>,
        val listener: ItemClickListener
    ) : RecyclerView.Adapter<ArtistCell>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistCell {
            return ArtistCell(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_detailled_item, parent, false)
            )
        }

        override fun onBindViewHolder(artistCell: ArtistCell, position: Int) {

            val artist = artist_list[position]


            Picasso.get().load(artist.strArtistThumb)
                .into(artistCell.artist_image)
            artistCell.artist_title.text = artist.strArtist

            artistCell.itemView.setOnClickListener {
                listener.onItemClicked(position)
            }
        }


        override fun getItemCount(): Int {
            return artist_list.size
        }

    }

    class ArtistCell(v: View) : RecyclerView.ViewHolder(v) {

        val artist_image = v.findViewById<ImageView>(R.id.row_item_image)
        val artist_title = v.findViewById<TextView>(R.id.row_item_title)


    }
}