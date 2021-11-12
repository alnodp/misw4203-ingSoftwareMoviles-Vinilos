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


    fun getCollector(onComplete:(resp: List<Collector>)->Unit, onError: (error: VolleyError)->Unit){
        serviceAdapter.getCollectors(onComplete, onError)
    }
}