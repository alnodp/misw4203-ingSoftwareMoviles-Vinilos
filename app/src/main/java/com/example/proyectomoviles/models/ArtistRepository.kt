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


    fun getArtist(onComplete:(resp: List<Artist>)->Unit, onError: (error: VolleyError)->Unit){
        serviceAdapter.getArtists(onComplete, onError)
    }
}