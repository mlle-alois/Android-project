package com.azimmermannrosenthal.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azimmermannrosenthal.myapplication.R
import com.azimmermannrosenthal.myapplication.listeners.ItemClickListener
import com.azimmermannrosenthal.myapplication.objects.Track

class TrackOfAlbumAdapter(
    private val tracks: List<Track>,
    private val listener: ItemClickListener
) : RecyclerView.Adapter<ListTrackCell>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListTrackCell {
        return ListTrackCell(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_simple_track, parent, false)
        )
    }

    override fun onBindViewHolder(listTrackCell: ListTrackCell, position: Int) {

        val track = tracks[position]

        listTrackCell.trackNumber.text = (position + 1).toString()
        listTrackCell.trackTitle.text = track.strTrack

        listTrackCell.itemView.setOnClickListener {
            listener.onItemClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}