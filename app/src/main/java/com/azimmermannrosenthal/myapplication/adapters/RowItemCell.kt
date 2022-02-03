package com.azimmermannrosenthal.myapplication.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.azimmermannrosenthal.myapplication.R

class RowItemCell(v: View) : RecyclerView.ViewHolder(v) {

    val rowItemImage: ImageView = v.findViewById(R.id.row_item_image)
    val rowItemTitle: TextView = v.findViewById(R.id.row_item_title)
    val rowItemDate: TextView = v.findViewById(R.id.row_item_date)

}