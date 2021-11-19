package com.example.proyectomoviles.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyectomoviles.models.*
import org.json.JSONArray
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
    fun getAlbums(onComplete:(resp:List<Album>)->Unit, onError: (error: VolleyError)->Unit){
        requestQueue.add(getRequest("albums", { response ->
            val resp = JSONArray(response)
            val list = mutableListOf<Album>()
            for (i in 0 until resp.length()) {
                val item = resp.getJSONObject(i)
                //TODO add tracks and comments

                val performers = mutableListOf<Performer>()
                if (item.has("performers")){
                    val performersJsonArray = item.getJSONArray("performers")
                    for(index in 0 until performersJsonArray.length()){
                        val perfItem = performersJsonArray.getJSONObject(index)
                        performers.add(
                            Performer(
                                id = perfItem.getInt("id"),
                                name = perfItem.getString("name"),
                                image = perfItem.getString("image"),
                                description = perfItem.getString("description"),
                                birthDate = if (perfItem.has("birthDate")) perfItem.getString("birthDate") else "",
                            )
                        )
                    }
                }

                list.add(
                    i,
                    Album(
                        id = item.getInt("id"),
                        name = item.getString("name"),
                        cover = item.getString("cover"),
                        recordLabel = item.getString("recordLabel"),
                        releaseDate = item.getString("releaseDate"),
                        genre = item.getString("genre"),
                        description = item.getString("description"),
                        performers = performers
                    )
                )
            }
            onComplete(list)
        }, {
            onError(it)
        }))
    }

    fun getArtists(onComplete:(resp:List<Artist>)->Unit, onError: (error: VolleyError)->Unit){
        requestQueue.add(getRequest("musicians", { response ->
            Log.d("getArtists ", response)
            val resp = JSONArray(response)
            val list = mutableListOf<Artist>()
            for (i in 0 until resp.length()) {
                val item = resp.getJSONObject(i)
                list.add(
                    i,
                    Artist(
                        id = item.getInt("id"),
                        name = item.getString("name"),
                        description = item.getString("description"),
                        image = item.getString("image"),
                        birthDate = item.getString("birthDate")
                    )
                )
            }
            onComplete(list)
        }, {
            onError(it)
        }))
    }

    fun getArtist(artistId: Int, onComplete:(resp:Artist)->Unit, onError: (error: VolleyError)->Unit){
        requestQueue.add(getRequest("musicians/$artistId", { response ->
            Log.d("getArtist $artistId ", response)
            val resp = JSONObject(response)

            val artist = Artist(
                id = resp.getInt("id"),
                name = resp.getString("name"),
                description = resp.getString("description"),
                image = resp.getString("image"),
                birthDate = resp.getString("birthDate")
            )

            onComplete(artist)
        }, {
            onError(it)
        }))
    }

    fun getArtistAlbums(artistId: Int, onComplete: (resp: List<Album>) -> Unit, onError: (error: VolleyError) -> Unit) {
        requestQueue.add(getRequest("musicians/$artistId/albums",
            { response ->
                Log.d("tagb", response)
                val resp = JSONArray(response)
                val list = mutableListOf<Album>()
                for (i in 0 until resp.length()) {
                    val albumItem = resp.getJSONObject(i)
                    list.add(i,
                        Album(id = albumItem.getInt("id"),
                            name = albumItem.getString("name"),
                            cover = albumItem.getString("cover"),
                            releaseDate = albumItem.getString("releaseDate"),
                            description = albumItem.getString("description"),
                            genre = albumItem.getString("genre"),
                            recordLabel = albumItem.getString("recordLabel")))

                }
                onComplete(list)
            },
            {
                onError(it)
            }))
    }

    fun getArtistPrizes(
        artistId: Int,
        onComplete: (resp: List<Prize>) -> Unit,
        onError: (error: VolleyError) -> Unit
    ) {
        requestQueue.add(
            getRequest("musicians/$artistId/",
                { response ->
                    Log.d("tagb", response)
                    val resp = JSONObject(response)
                    val list = mutableListOf<Prize>()

                    if (resp.has("performerPrizes")) {
                        val prizesJsonArray = resp.getJSONArray("performerPrizes")
                        for (index in 0 until prizesJsonArray.length()) {
                            val perfItem = prizesJsonArray.getJSONObject(index)
                            list.add(
                                Prize(
                                    id = perfItem.getInt("id"),
                                    premiationDate = perfItem.getString("premiationDate")
                                )
                            )
                        }
                    }

                    onComplete(list)
                },
                {
                    onError(it)
                })
        )
    }

    fun getPrize(
        prizeId: Int,
        onComplete: (resp: Prize) -> Unit,
        onError: (error: VolleyError) -> Unit
    ) {
        requestQueue.add(getRequest("prizes/$prizeId", { response ->
            Log.d("getPrize $prizeId ", response)
            val resp = JSONObject(response)

            val prize = Prize(
                id = prizeId,
                name = resp.getString("name"),
                organization = resp.getString("organization"),
                description = resp.getString("description")
            )

            onComplete(prize)
        }, {
            onError(it)
        }))
    }

    fun getAlbum(albumId: Int, onComplete: (resp: Album) -> Unit, onError: (error: VolleyError) -> Unit) {
        requestQueue.add(getRequest("albums/$albumId",
            { response ->
                Log.d("tagb", response)
                val resp = JSONObject(response)

                val commentsList: JSONArray = resp.getJSONArray("comments")
                val comments = mutableListOf<Comment>()
                var comment: JSONObject? = null

                for (i in 0 until commentsList.length()) {
                    comment = commentsList.getJSONObject(i)
                    comments.add(
                        Comment(comment.getString("description"),
                            comment.getInt("rating").toString(), albumId)
                    )
                }

                val performers = mutableListOf<Performer>()
                if (resp.has("performers")){
                    val performersJsonArray = resp.getJSONArray("performers")
                    for(index in 0 until performersJsonArray.length()){
                        val perfItem = performersJsonArray.getJSONObject(index)
                        performers.add(
                            Performer(
                                id = perfItem.getInt("id"),
                                name = perfItem.getString("name"),
                                image = perfItem.getString("image"),
                                description = perfItem.getString("description"),
                                birthDate = if (perfItem.has("birthDate")) perfItem.getString("birthDate") else "",
                            )
                        )
                    }
                }

                val tracksList: JSONArray = resp.getJSONArray("tracks")
                val tracks = mutableListOf<Track>()
                var track: JSONObject? = null

                for (i in 0 until tracksList.length()) {
                    track = tracksList.getJSONObject(i)
                    tracks.add(
                        Track(
                            track.getInt("id"),
                            track.getString("name"),
                            track.getString("duration")
                        )
                    )
                }

                val album: Album = Album(
                    id = resp.getInt("id"),
                    name = resp.getString("name"),
                    cover = resp.getString("cover"),
                    recordLabel = resp.getString("recordLabel"),
                    releaseDate = resp.getString("releaseDate"),
                    genre = resp.getString("genre"),
                    description = resp.getString("description"),
                    comments = comments,
                    performers = performers,
                    tracks = tracks
                )

                onComplete(album)
            },
            {
                onError(it)
            }))
    }

    fun getCollectors(onComplete:(resp:List<Collector>)->Unit, onError: (error:VolleyError)->Unit) {
        requestQueue.add(getRequest("collectors",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Collector>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    list.add(i, Collector(collectorId = item.getInt("id"),name = item.getString("name"),
                        telephone = item.getString("telephone"), email = item.getString("email")))
                }
                onComplete(list)
            },
            {
                onError(it)
            }))
    }

    fun getCollector(collectorId: Int, onComplete: (resp: Collector) -> Unit, onError: (error: VolleyError) -> Unit) {
        requestQueue.add(getRequest("collectors/$collectorId",
            { response ->
                Log.d("tagb", response)
                val resp = JSONObject(response)

                val commentsList: JSONArray = resp.getJSONArray("comments")
                val comments = mutableListOf<Comment>()
                var comment: JSONObject? = null

                for (i in 0 until commentsList.length()) {
                    comment = commentsList.getJSONObject(i)
                    comments.add(
                        Comment(comment.getString("description"),
                            comment.getInt("rating").toString(), collectorId)
                    )
                }

                val favPerformers = mutableListOf<Performer>()
                if (resp.has("favoritePerformers")){
                    val performersJsonArray = resp.getJSONArray("favoritePerformers")
                    for(index in 0 until performersJsonArray.length()){
                        val perfItem = performersJsonArray.getJSONObject(index)
                        favPerformers.add(
                            Performer(
                                id = perfItem.getInt("id"),
                                name = perfItem.getString("name"),
                                image = perfItem.getString("image"),
                                description = perfItem.getString("description"),
                                birthDate = if (perfItem.has("birthDate")) perfItem.getString("birthDate") else "",
                            )
                        )
                    }
                }

                val albumList: JSONArray = resp.getJSONArray("collectorAlbums")
                val albums = mutableListOf<Album>()
                var album: JSONObject? = null

                for (i in 0 until albumList.length()) {
                    album = albumList.getJSONObject(i)
                    albums.add(
                        Album(
                            id = album.getInt("id"),
                            name = if (album.has("name")) album.getString("name") else "",
                            cover = if (album.has("cover")) album.getString("cover") else "",
                            recordLabel = if (album.has("recordLabel")) album.getString("recordLabel") else "",
                            releaseDate = if (album.has("releaseDate")) album.getString("releaseDate") else "",
                            genre = if (album.has("genre")) album.getString("genre") else "",
                            description = if (album.has("description")) album.getString("description") else "",
                            price = album.getString("price"),
                            status = album.getString("status")
                        )
                    )
                }

                val collector: Collector = Collector(
                    collectorId = resp.getInt("id"),
                    name = resp.getString("name"),
                    telephone = resp.getString("telephone"),
                    email = resp.getString("email"),
                    comments = comments,
                    favoritePerformers = favPerformers,
                    collectorAlbums = albums
                )

                onComplete(collector)
            },
            {
                onError(it)
            }))
    }

    fun getCollectorAlbums(collectorId: Int, onComplete: (resp: List<Album>) -> Unit, onError: (error: VolleyError) -> Unit) {
        requestQueue.add(getRequest("collectors/$collectorId/albums",
            { response ->
                Log.d("tagb", response)
                val resp = JSONArray(response)
                val list = mutableListOf<Album>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)

                    if (item.has("album")){
                        val albumItem = item.getJSONObject("album")
                        list.add(i,
                            Album(id = albumItem.getInt("id"),
                                name = albumItem.getString("name"),
                                price = item.getString("price"),
                                status = item.getString("status"),
                                cover = albumItem.getString("cover"),
                                description = albumItem.getString("description"),
                                genre = albumItem.getString("genre"),
                                recordLabel = albumItem.getString("recordLabel"),
                                releaseDate = albumItem.getString("releaseDate")))
                    }

                }
                onComplete(list)
            },
            {
                onError(it)
            }))
    }

    /*
    fun getComments( albumId:Int, onComplete:(resp:List<Comment>)->Unit , onError: (error:VolleyError)->Unit) {
        requestQueue.add(getRequest("albums/$albumId/comments",
            Response.Listener<String> { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Comment>()
                var item: JSONObject? = null
                for (i in 0 until resp.length()) {
                    item = resp.getJSONObject(i)
                    Log.d("Response", item.toString())
                    list.add(i, Comment(albumId = albumId, rating = item.getInt("rating"), description = item.getString("description")))
                    Log.d("Rating", Comment(albumId = albumId, rating = item.getInt("rating"), description = item.getString("description")).rating.toString())
                }
                onComplete(list)
            },
            Response.ErrorListener {
                onError(it)
            }))
    }
    fun postComment(body: JSONObject, albumId: Int, onComplete:(resp: JSONObject)->Unit, onError: (error:VolleyError)->Unit){
        requestQueue.add(postRequest("albums/$albumId/comments",
            body,
            Response.Listener<JSONObject> { response ->
                onComplete(response)
            },
            Response.ErrorListener {
                onError(it)
            }))
    }

     */
    private fun getRequest(path:String, responseListener: Response.Listener<String>, errorListener: Response.ErrorListener): StringRequest {
        return StringRequest(
            Request.Method.GET,
            BASE_URL + path,
            responseListener,
            errorListener
        )
    }
    private fun postRequest(path: String, body: JSONObject, responseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener ):JsonObjectRequest{
        return  JsonObjectRequest(
            Request.Method.POST,
            BASE_URL + path,
            body,
            responseListener,
            errorListener
        )
    }
    private fun putRequest(path: String, body: JSONObject, responseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener ):JsonObjectRequest{
        return  JsonObjectRequest(
            Request.Method.PUT,
            BASE_URL + path,
            body,
            responseListener,
            errorListener
        )
    }
}