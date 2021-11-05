package com.example.proyectomoviles.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyectomoviles.models.Artist
import org.json.JSONObject

class NetworkServiceAdapter constructor(context: Context) {
    companion object{
        const val BASE_URL= "https://back-vinyls-populated.herokuapp.com/"
        var instance: NetworkServiceAdapter? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: NetworkServiceAdapter(context).also {
                    instance = it
                }
            }
    }
    private val requestQueue: RequestQueue by lazy {
        // applicationContext keeps you from leaking the Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }

    private fun getRequest(path:String, responseListener: Response.Listener<String>, errorListener: Response.ErrorListener): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL+path, responseListener,errorListener)
    }


    fun getArtist(onComplete:(resp:Artist)->Unit , onError: (error: VolleyError)->Unit) {
        requestQueue.add(getRequest("musicians/",
            Response.Listener<String> { response ->
                val resp = JSONObject(response)
                val album = Artist(name=resp.getString("name"),id = resp.getString("id"), image = resp.getString("image"),
                    description = resp.getString("description"), creationDate = resp.getString("creationDate"))
                onComplete(album)
            },
            Response.ErrorListener {
                onError(it)
            }))
    }
}