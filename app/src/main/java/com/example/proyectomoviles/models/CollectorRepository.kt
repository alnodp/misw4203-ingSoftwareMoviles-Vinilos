package com.example.proyectomoviles.models

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.proyectomoviles.network.CacheManager
import com.example.proyectomoviles.network.NetworkServiceAdapter

class CollectorRepository (val context: Context)  {
    private var serviceAdapter: NetworkServiceAdapter = NetworkServiceAdapter.getInstance(context)

    companion object {
        @SuppressLint("StaticFieldLeak")
        var instance: CollectorRepository? = null
        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: CollectorRepository(application).also {
                    instance = it
                }
            }
    }


    suspend fun getCollectors(): List<Collector>{
        val potentialResp = CacheManager.getInstance(context).getCollectors()
        return if(potentialResp.isEmpty()){
            val collectors = serviceAdapter.getCollectors()
            CacheManager.getInstance(context).addCollectors(collectors)
            collectors
        } else{
            potentialResp
        }
    }

    suspend fun getCollector(collectorId: Int): Collector {
        return serviceAdapter.getCollector(collectorId)
    }

    suspend fun getCollectorAlbums(collectorId: Int): List<Album> {
        return serviceAdapter.getCollectorAlbums(collectorId)
    }
}