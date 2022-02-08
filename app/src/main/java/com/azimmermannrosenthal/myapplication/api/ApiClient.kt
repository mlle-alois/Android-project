package com.azimmermannrosenthal.myapplication.api

import com.azimmermannrosenthal.myapplication.api.recuperation_lists.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object ApiClient {
    private const val BASE_URL: String = "https://theaudiodb.com/api/v1/json/523532/"

    /*private val gson : Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val httpClient : OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService : ApiService by lazy{
        retrofit.create(ApiService::class.java)
    }*/

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(ApiService::class.java)

    fun getMostLovedTracksAsync(): Deferred<LovedTrackList> {
        return api.getMostLovedTracksAsync()
    }

    fun getMostLovedAlbumsAsync(): Deferred<LovedAlbumList> {
        return api.getMostLovedAlbumsAsync()
    }

    fun searchArtistByNameAsync(artist: String): Deferred<FoundedArtistList> {
        return api.searchArtistByNameAsync(artist)
    }

    fun searchArtistByIdAsync(artist: String): Deferred<FoundedArtistList> {
        return api.searchArtistByIdAsync(artist)
    }

    fun getAlbumsByIdAsync(albumId: String): Deferred<AlbumList> {
        return api.getAlbumsByIdAsync(albumId)
    }

    fun getAlbumsByArtistIdAsync(artistId: String): Deferred<AlbumList> {
        return api.getAlbumsByArtistIdAsync(artistId)
    }

    fun getTop10TracksByArtistNameAsync(artist: String): Deferred<TrackList> {
        return api.getTop10TracksByArtistNameAsync(artist)
    }

    fun getTracksByAlbumIdAsync(albumId: String): Deferred<TrackList> {
        return api.getTracksByAlbumIdAsync(albumId)
    }
}