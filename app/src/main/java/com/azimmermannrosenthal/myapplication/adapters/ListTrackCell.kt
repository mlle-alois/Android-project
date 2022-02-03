package com.azimmermannrosenthal.myapplication.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.azimmermannrosenthal.myapplication.R

class ListTrackCell(v: View) : RecyclerView.ViewHolder(v) {

    val trackNumber: TextView = v.findViewById(R.id.track_number)
    val trackTitle: TextView = v.findViewById(R.id.track_title)

}