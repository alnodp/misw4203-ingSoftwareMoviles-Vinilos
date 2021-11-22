package com.example.proyectomoviles.models

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.proyectomoviles.network.CacheManager
import com.example.proyectomoviles.network.NetworkServiceAdapter

class ArtistRepository(val context: Context)  {
    private var serviceAdapter: NetworkServiceAdapter = NetworkServiceAdapter.getInstance(context)

    companion object {
        @SuppressLint("StaticFieldLeak")
        var instance: ArtistRepository? = null
        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: ArtistRepository(application).also {
                    instance = it
                }
            }
    }

    suspend fun getArtists(): List<Artist> {
        val potentialResp = CacheManager.getInstance(context).getArtists()
        return if(potentialResp.isEmpty()){
            val artists = serviceAdapter.getArtists()
            CacheManager.getInstance(context).addArtists(artists)
            artists
        } else{
            potentialResp
        }
    }

    suspend fun getArtist(artistId: Int): Artist {
        return serviceAdapter.getArtist(artistId)
    }

    suspend fun getArtistAlbums(artistId: Int): List<Album> {
        return serviceAdapter.getArtistAlbums(artistId)
    }

    suspend fun getArtistPrizes(artistId: Int): List<Prize> {
        return serviceAdapter.getArtistPrizes(artistId)
    }

    suspend fun getPrize(prizeId: Int): Prize {
        return serviceAdapter.getPrize(prizeId)
    }
}