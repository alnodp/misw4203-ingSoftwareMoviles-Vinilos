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

    suspend fun getAlbums(invalidateCache: Boolean = false): List<Album>{

        if (!invalidateCache) {
            var potentialResp = CacheManager.getInstance(context).getAlbums()
            if (potentialResp.isNotEmpty()) {
                return potentialResp
            }
        }
        return getAlbumsFromNet()
    }

    suspend fun getAlbumsFromNet(): List<Album>{
        this.resetCache()
        val albums = serviceAdapter.getAlbums()

        CacheManager.getInstance(context).addAlbums(albums)
        return albums
    }

    fun resetCache() {
        CacheManager.getInstance(context).resetAlbumCache()
        serviceAdapter.resetCache()
    }

    suspend fun addAlbum(album: Album): Album {
        return serviceAdapter.addAlbum(album)
    }

    suspend fun getAlbum(albumId: Int): Album {
        return serviceAdapter.getAlbum(albumId)
    }

    suspend fun addAlbumTrack(albumId: Int, track: Track): Track {
        return serviceAdapter.addTrack(albumId, track)
    }
}