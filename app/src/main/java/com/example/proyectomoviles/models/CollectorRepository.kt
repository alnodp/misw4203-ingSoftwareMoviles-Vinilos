package com.example.proyectomoviles.models

import android.app.Application
import com.android.volley.VolleyError
import com.example.proyectomoviles.network.NetworkServiceAdapter

class CollectorRepository (application: Application)  {

    private var serviceAdapter: NetworkServiceAdapter

    companion object {
        var instance: CollectorRepository? = null
        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: CollectorRepository(application).also {
                    instance = it
                }
            }
    }

    init {
        serviceAdapter = NetworkServiceAdapter.getInstance(application)
    }


    suspend fun getCollectors(): List<Collector>{
        return serviceAdapter.getCollectors()
    }

    suspend fun getCollector(collectorId: Int): Collector {
        return serviceAdapter.getCollector(collectorId)
    }

    suspend fun getCollectorAlbums(collectorId: Int): List<Album> {
        return serviceAdapter.getCollectorAlbums(collectorId)
    }
}