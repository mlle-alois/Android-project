package com.azimmermannrosenthal.myapplication.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.azimmermannrosenthal.myapplication.R

class ArtistCell(v: View) : RecyclerView.ViewHolder(v) {

    val artistImage: ImageView = v.findViewById(R.id.row_item_image)
    val artistTitle: TextView = v.findViewById(R.id.row_item_title)

}