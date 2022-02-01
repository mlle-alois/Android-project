package com.azimmermannrosenthal.myapplication.api


import com.azimmermannrosenthal.myapplication.api.recuperation_lists.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("mostloved.php?format=track")
    suspend fun getMostLovedTracks(): Response<LovedTrackList>

    @GET("mostloved.php?format=album")
    suspend fun getMostLovedAlbums(): Response<LovedAlbumList>

    @GET("search.php")
    suspend fun searchArtistByName(@Query("s") artist : String): Response<FoundedArtistList> //TODO ajouter le % à la fin du nom de l'artiste

    @GET("artist.php")
    suspend fun searchArtistById(@Query("i") artist : String): Response<FoundedArtistList>

    @GET("album.php")
    suspend fun getAlbumsById(@Query("m") albumId : String): Response<AlbumList>

    @GET("album.php")
    suspend fun getAlbumsByArtistId(@Query("i") artistId : String): Response<AlbumList>

    @GET("track-top10.php?")
    suspend fun getTop10TracksByArtistName(@Query("s") artist: String): Response<TrackList>

    @GET("track.php?")
    suspend fun getTracksByAlbumId(@Query("m") albumId: String): Response<TrackList>
}