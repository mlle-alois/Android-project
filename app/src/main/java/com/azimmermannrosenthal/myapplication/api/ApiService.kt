package com.azimmermannrosenthal.myapplication.api


import com.azimmermannrosenthal.myapplication.api.recuperation_lists.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("mostloved.php?format=track")
    fun getMostLovedTracksAsync(): Deferred<LovedTrackList>

    @GET("mostloved.php?format=album")
    fun getMostLovedAlbumsAsync(): Deferred<LovedAlbumList>

    @GET("search.php")
    fun searchArtistByNameAsync(@Query("s") artist : String): Deferred<FoundedArtistList>

    @GET("artist.php")
    fun searchArtistByIdAsync(@Query("i") artist : String): Deferred<FoundedArtistList>

    @GET("album.php")
    fun getAlbumsByIdAsync(@Query("m") albumId : String): Deferred<AlbumList>

    @GET("album.php")
    fun getAlbumsByArtistIdAsync(@Query("i") artistId : String): Deferred<AlbumList>

    @GET("track-top10.php?")
    fun getTop10TracksByArtistNameAsync(@Query("s") artist: String): Deferred<TrackList>

    @GET("track.php?")
    fun getTracksByAlbumIdAsync(@Query("m") albumId: String): Deferred<TrackList>
}