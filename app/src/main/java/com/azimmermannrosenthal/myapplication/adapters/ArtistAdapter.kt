package com.azimmermannrosenthal.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azimmermannrosenthal.myapplication.R
import com.azimmermannrosenthal.myapplication.listeners.ItemClickListener
import com.azimmermannrosenthal.myapplication.objects.Artist
import com.squareup.picasso.Picasso

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

        Picasso.get().load(artist.strArtistThumb).into(artistCell.artistImage)
        artistCell.artistTitle.text = artist.strArtist

        artistCell.itemView.setOnClickListener {
            listener.onItemClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return artist_list.size
    }
}