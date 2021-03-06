package com.azimmermannrosenthal.myapplication.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azimmermannrosenthal.myapplication.listeners.ItemClickListener
import com.azimmermannrosenthal.myapplication.R
import com.azimmermannrosenthal.myapplication.adapters.*
import com.azimmermannrosenthal.myapplication.api.ApiClient
import com.azimmermannrosenthal.myapplication.objects.Album
import com.azimmermannrosenthal.myapplication.objects.Artist
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeSearchFragment : Fragment() {

    private var albums = listOf<Album>()
    private var artists = listOf<Artist>()

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
            getString(R.string.to_research)

        (activity as AppCompatActivity).findViewById<BottomNavigationView>(R.id.home_nav).visibility =
            View.VISIBLE

        if(artists.isEmpty()) {
            view.findViewById<TextView>(R.id.artists).visibility = View.GONE
        }
        if(albums.isEmpty()) {
            view.findViewById<TextView>(R.id.albums).visibility = View.GONE
        }

        clearSearchListener(view)

        if(!isNetworkConnected())
            return

        setOnTextChangeListener(view)

    }

    private fun setOnTextChangeListener(view: View) {
        val editText = view.findViewById<View>(R.id.plain_text_input) as EditText
        editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                if (s != "") {
                    MainScope().launch(Dispatchers.Main) {

                        view.findViewById<ProgressBar>(R.id.progressBarArtists).visibility = View.VISIBLE

                        artists = withContext(Dispatchers.Main) {
                            ApiClient.searchArtistByNameAsync(s.toString() + "%")
                        }.await().list

                        setArtists(view, artists)

                        view.findViewById<ProgressBar>(R.id.progressBarArtists).visibility = View.GONE

                        if(artists.isNullOrEmpty()) {
                            view.findViewById<TextView>(R.id.artists).visibility = View.GONE
                        } else {
                            view.findViewById<TextView>(R.id.artists).visibility = View.VISIBLE
                        }
                    }

                    if (!artists.isNullOrEmpty()) {

                        MainScope().launch(Dispatchers.Main) {

                            view.findViewById<ProgressBar>(R.id.progressBarAlbums).visibility = View.VISIBLE

                            albums = withContext(Dispatchers.Main) {
                                ApiClient.getAlbumsByArtistIdAsync(artists.takeLast(1)[0].idArtist)
                            }.await().list

                            setAlbums(view, albums)

                            view.findViewById<ProgressBar>(R.id.progressBarAlbums).visibility = View.GONE

                            if(albums.isNullOrEmpty()) {
                                view.findViewById<TextView>(R.id.albums).visibility = View.GONE
                            } else {
                                view.findViewById<TextView>(R.id.albums).visibility = View.VISIBLE
                            }
                        }
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
                adapter = ItemAlbumAdapter(
                    albums,
                    listener = object : ItemClickListener {
                        override fun onItemClicked(position: Int) {
                            findNavController().navigate(
                                HomeSearchFragmentDirections.actionTabSearchToAlbumFragment(albums[position])
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
       if(this.artists.isNullOrEmpty()){

           Snackbar.make(view, "No artist found", 3000 ).show()
           view.findViewById<RecyclerView>(R.id.artist_list).visibility = View.GONE

           view.findViewById<RecyclerView>(R.id.album_list).visibility = View.GONE
           view.findViewById<TextView>(R.id.albums).visibility = View.GONE
           return
        }

        if (this.artists.isNotEmpty()) {
            view.findViewById<RecyclerView>(R.id.artist_list).visibility = View.VISIBLE

            view.findViewById<RecyclerView>(R.id.album_list).visibility = View.VISIBLE
            view.findViewById<RecyclerView>(R.id.artist_list).run {
                adapter = ItemArtistAdapter(
                    artists,
                    listener = object : ItemClickListener {
                        override fun onItemClicked(position: Int) {
                            findNavController().navigate(
                                HomeSearchFragmentDirections.actionTabSearchToArtistFragment(artists[position])
                            )
                        }
                    }
                )
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }
    private fun isNetworkConnected(): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }
}