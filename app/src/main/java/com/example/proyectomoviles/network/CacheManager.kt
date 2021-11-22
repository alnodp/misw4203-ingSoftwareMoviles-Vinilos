package com.example.proyectomoviles.network

import android.content.Context
import com.example.proyectomoviles.models.Album
import com.example.proyectomoviles.models.Artist
import com.example.proyectomoviles.models.Collector

class CacheManager(context: Context) {
    companion object{
        var instance: CacheManager? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: CacheManager(context).also {
                    instance = it
                }
            }
    }

    private var albums: List<Album> = mutableListOf<Album>()
    private var artists: List<Artist> = mutableListOf<Artist>()
    private var collectors: List<Collector> = mutableListOf<Collector>()

    fun addAlbums(newAlbums: List<Album>){
        if (albums.isEmpty()){
            albums = newAlbums
        }
    }

    fun getAlbums() : List<Album>{
        return if (albums.isEmpty()) listOf<Album>() else albums
    }

    fun addArtists(newArtists: List<Artist>){
        if (artists.isEmpty()){
            artists = newArtists
        }
    }

    fun getArtists() : List<Artist>{
        return if (artists.isEmpty()) listOf<Artist>() else artists
    }

    fun addCollectors(newCollectors: List<Collector>){
        if (collectors.isEmpty()){
            collectors = newCollectors
        }
    }

    fun getCollectors() : List<Collector>{
        return if (collectors.isEmpty()) listOf<Collector>() else collectors
    }
}