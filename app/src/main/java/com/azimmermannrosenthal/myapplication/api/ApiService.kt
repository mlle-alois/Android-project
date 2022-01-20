package com.azimmermannrosenthal.myapplication.api


import com.azimmermannrosenthal.myapplication.api.recuperation_lists.AlbumList
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.FoundedArtistList
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.LovedAlbumList
import com.azimmermannrosenthal.myapplication.api.recuperation_lists.LovedTrackList
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("mostloved.php?format=track")
    suspend fun getMostLovedTracks(): Response<LovedTrackList>

    @GET("mostloved.php?format=album")
    suspend fun getMostLovedAlbums(): Response<LovedAlbumList>

    @GET("search.php")
    suspend fun searchArtistByName(@Query("s") artist : String): Response<FoundedArtistList> //TODO ajouter le % à la fin du nom de l'artiste

    @GET("album.php")
    suspend fun getAlbumsByArtistId(@Query("i") artistId : String): Response<AlbumList>

    @GET("track-top10.php?")
    suspend fun getTop10TracksByArtistName(@Query("s") artist: String): Response<TrackList>

    @GET("track.php?")
    suspend fun getTracksByAlbumId(@Query("m") albumId: String): Response<TrackList>
}