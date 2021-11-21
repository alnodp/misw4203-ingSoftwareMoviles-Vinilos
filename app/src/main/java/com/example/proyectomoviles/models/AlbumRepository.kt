package com.example.proyectomoviles.models

import android.app.Application
import android.content.Context
import com.android.volley.VolleyError
import com.example.proyectomoviles.network.NetworkServiceAdapter

class AlbumRepository(context: Context) {
    private var networkServiceAdapter: NetworkServiceAdapter = NetworkServiceAdapter.getInstance(context)

    companion object {
        var instance: AlbumRepository? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: AlbumRepository(context).also {
                    instance = it
                }
            }
    }

    suspend fun getAlbums(): List<Album>{
        return networkServiceAdapter.getAlbums()
    }

    suspend fun getAlbum(albumId: Int): Album {
        return networkServiceAdapter.getAlbum(albumId)
    }
}