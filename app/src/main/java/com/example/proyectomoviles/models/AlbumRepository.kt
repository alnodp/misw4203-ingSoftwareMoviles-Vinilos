package com.example.proyectomoviles.models

import android.annotation.SuppressLint
import android.content.Context
import com.example.proyectomoviles.network.CacheManager
import com.example.proyectomoviles.network.NetworkServiceAdapter

class AlbumRepository(val context: Context) {
    private var serviceAdapter: NetworkServiceAdapter = NetworkServiceAdapter.getInstance(context)

    companion object {
        @SuppressLint("StaticFieldLeak")
        var instance: AlbumRepository? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: AlbumRepository(context).also {
                    instance = it
                }
            }
    }

    suspend fun getAlbums(): List<Album>{
        val potentialResp = CacheManager.getInstance(context).getAlbums()
        return if(potentialResp.isEmpty()){
            val albums = serviceAdapter.getAlbums()
            CacheManager.getInstance(context).addAlbums(albums)
            albums
        } else{
            potentialResp
        }
    }

    suspend fun getAlbum(albumId: Int): Album {
        return serviceAdapter.getAlbum(albumId)
    }

    suspend fun addAlbumTrack(albumId: Int, track: Track): Track {
        return serviceAdapter.addTrack(albumId, track)
    }
}