package com.azimmermannrosenthal.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azimmermannrosenthal.myapplication.R
import com.azimmermannrosenthal.myapplication.listeners.ItemClickListener
import com.azimmermannrosenthal.myapplication.objects.Artist
import com.squareup.picasso.Picasso

class ItemArtistAdapter(
    private val artist_list: List<Artist>,
    val listener: ItemClickListener
) : RecyclerView.Adapter<RowItemCell>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowItemCell {
        return RowItemCell(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_detailled_item, parent, false)
        )
    }

    override fun onBindViewHolder(artistCell: RowItemCell, position: Int) {

        val artist = artist_list[position]

        Picasso.get().load(artist.strArtistThumb).into(artistCell.rowItemImage)
        artistCell.rowItemTitle.text = artist.strArtist
        artistCell.rowItemDate.visibility = View.GONE

        artistCell.itemView.setOnClickListener {
            listener.onItemClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return artist_list.size
    }
}