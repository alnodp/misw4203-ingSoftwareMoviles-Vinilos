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

    fun getAlbums(onComplete:(resp:List<Album>)->Unit, onError: (error: VolleyError)->Unit){
        networkServiceAdapter.getAlbums(onComplete, onError)
    }

    fun getAlbum(albumId: Int, onComplete: (resp:Album)->Unit, onError: (error: VolleyError)->Unit) {
        networkServiceAdapter.getAlbum(albumId, onComplete, onError)
    }
}