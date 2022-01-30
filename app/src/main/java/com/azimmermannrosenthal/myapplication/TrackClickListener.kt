package com.azimmermannrosenthal.myapplication

interface TrackClickListener {
    fun onTrackClicked(position: Int)
    fun onAlbumClicked(position: Int)
    fun onArtistClicked(position: Int)
}