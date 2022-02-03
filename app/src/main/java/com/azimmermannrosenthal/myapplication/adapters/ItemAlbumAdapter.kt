package com.azimmermannrosenthal.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azimmermannrosenthal.myapplication.R
import com.azimmermannrosenthal.myapplication.listeners.ItemClickListener
import com.azimmermannrosenthal.myapplication.objects.Album
import com.squareup.picasso.Picasso

class ItemAlbumAdapter(
    private val albums: List<Album>,
    private val listener: ItemClickListener
) : RecyclerView.Adapter<RowItemCell>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowItemCell {
        return RowItemCell(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_detailled_item, parent, false)
        )
    }

    override fun onBindViewHolder(rowItemCell: RowItemCell, position: Int) {

        val album = albums[position]

        Picasso.get().load(album.strAlbumThumb).into(rowItemCell.rowItemImage)
        rowItemCell.rowItemTitle.text = album.strAlbum
        rowItemCell.rowItemDate.text = album.intYearReleased

        rowItemCell.itemView.setOnClickListener {
            listener.onItemClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return albums.size
    }

}