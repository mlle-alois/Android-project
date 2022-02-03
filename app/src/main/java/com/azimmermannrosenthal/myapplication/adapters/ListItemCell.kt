package com.azimmermannrosenthal.myapplication.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.azimmermannrosenthal.myapplication.R

class ListItemCell(v: View) : RecyclerView.ViewHolder(v) {

    val itemNumber: TextView = v.findViewById(R.id.item_number)
    val itemImage: ImageView = v.findViewById(R.id.item_image)
    val itemTitle: TextView = v.findViewById(R.id.item_title)
    val itemArtist: TextView = v.findViewById(R.id.item_artist)

}