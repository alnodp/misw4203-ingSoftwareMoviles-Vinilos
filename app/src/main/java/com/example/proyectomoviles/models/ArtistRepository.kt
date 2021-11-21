package com.example.proyectomoviles.models

import android.app.Application
import com.android.volley.VolleyError
import com.example.proyectomoviles.network.NetworkServiceAdapter

class ArtistRepository(application: Application)  {
    private var serviceAdapter: NetworkServiceAdapter

    companion object {
        var instance: ArtistRepository? = null
        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: ArtistRepository(application).also {
                    instance = it
                }
            }
    }

    init {
        serviceAdapter = NetworkServiceAdapter.getInstance(application)
    }

    suspend fun getArtists(): List<Artist> {
        return serviceAdapter.getArtists()
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