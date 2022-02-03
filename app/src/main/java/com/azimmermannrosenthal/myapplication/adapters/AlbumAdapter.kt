package com.azimmermannrosenthal.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azimmermannrosenthal.myapplication.R
import com.azimmermannrosenthal.myapplication.listeners.AlbumClickListener
import com.azimmermannrosenthal.myapplication.objects.Album
import com.squareup.picasso.Picasso

class AlbumAdapter(
    private val albums: List<Album>,
    private val listener: AlbumClickListener
) : RecyclerView.Adapter<ListItemCell>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemCell {
        return ListItemCell(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_home, parent, false)
        )
    }

    override fun onBindViewHolder(listItemCell: ListItemCell, position: Int) {

        val track = albums[position]

        listItemCell.itemNumber.text = (position + 1).toString()
        Picasso.get().load(track.strAlbumThumb).into(listItemCell.itemImage)
        listItemCell.itemTitle.text = track.strAlbum
        listItemCell.itemArtist.text = track.strArtist

        listItemCell.itemView.setOnClickListener {
            listener.onAlbumClicked(position)
        }
        listItemCell.itemArtist.setOnClickListener {
            listener.onArtistClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return albums.size
    }
}