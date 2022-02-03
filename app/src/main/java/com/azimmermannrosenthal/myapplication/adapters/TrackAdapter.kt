package com.azimmermannrosenthal.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azimmermannrosenthal.myapplication.R
import com.azimmermannrosenthal.myapplication.listeners.TrackClickListener
import com.azimmermannrosenthal.myapplication.objects.Track
import com.squareup.picasso.Picasso

class TrackAdapter(
    private val tracks: List<Track>,
    private val listener: TrackClickListener
) : RecyclerView.Adapter<ListItemCell>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemCell {
        return ListItemCell(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_home, parent, false)
        )
    }

    override fun onBindViewHolder(listItemCell: ListItemCell, position: Int) {

        val track = tracks[position]

        listItemCell.itemNumber.text = (position + 1).toString()
        Picasso.get().load(track.strTrackThumb).into(listItemCell.itemImage)
        listItemCell.itemTitle.text = track.strTrack
        listItemCell.itemArtist.text = track.strArtist

        listItemCell.itemArtist.setOnClickListener {
            listener.onArtistClicked(position)
        }
        listItemCell.itemImage.setOnClickListener {
            listener.onTrackClicked(position)
        }
        listItemCell.itemView.setOnClickListener {
            listener.onTrackClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}