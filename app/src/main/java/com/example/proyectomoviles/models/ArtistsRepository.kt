package com.example.proyectomoviles.models

import android.app.Application
import com.android.volley.VolleyError
import com.example.proyectomoviles.network.NetworkServiceAdapter

class ArtistsRepository(application: Application)  {
    private var serviceAdapter: NetworkServiceAdapter

    companion object {
        var instance: ArtistsRepository? = null
        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: ArtistsRepository(application).also {
                    instance = it
                }
            }
    }

    init {
        serviceAdapter = NetworkServiceAdapter.getInstance(application)
    }


    fun getArtist(onComplete:(resp: List<Artist>)->Unit, onError: (error: VolleyError)->Unit){
        serviceAdapter.getArtists(onComplete, onError)
    }
}