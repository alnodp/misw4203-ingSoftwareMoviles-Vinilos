package com.example.proyectomoviles.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.proyectomoviles.models.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

    fun resetCache() {
        requestQueue.cache.clear()
    }

    suspend fun getAlbums() = suspendCoroutine<List<Album>> { cont ->
        val list = mutableListOf<Album>()
        requestQueue.add(getRequest("albums", { response ->
            val resp = JSONArray(response)
            for (i in 0 until resp.length()) {
                val item = resp.getJSONObject(i)

                list.add(
                    i,
                    Album(
                        id = item.getInt("id"),
                        name = item.getString("name"),
                        cover = item.getString("cover"),
                        recordLabel = item.getString("recordLabel"),
                        releaseDate = item.getString("releaseDate"),
                        genre = item.getString("genre"),
                        description = item.getString("description")
                    )
                )
            }
            cont.resume(list)
        }, {
            cont.resumeWithException(it)
        }))
    }

    suspend fun getArtists() = suspendCoroutine<List<Artist>> { cont ->
        val list = mutableListOf<Artist>()
        requestQueue.add(getRequest("musicians", { response ->
            Log.d("getArtists ", response)
            val resp = JSONArray(response)
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
            cont.resume(list)
        }, {
            cont.resumeWithException(it)
        }))
    }

    suspend fun getArtist(artistId: Int) = suspendCoroutine<Artist>{ cont ->
        var artist: Artist? = null
        requestQueue.add(getRequest("musicians/$artistId", { response ->
            Log.d("getArtist $artistId ", response)
            val resp = JSONObject(response)

            artist = Artist(
                id = resp.getInt("id"),
                name = resp.getString("name"),
                description = resp.getString("description"),
                image = resp.getString("image"),
                birthDate = resp.getString("birthDate")
            )

            cont.resume(artist!!)
        }, {
            cont.resumeWithException(it)
        }))
    }

    suspend fun getArtistAlbums(artistId: Int) = suspendCoroutine<List<Album>> { cont ->
        val list = mutableListOf<Album>()
        requestQueue.add(getRequest("musicians/$artistId/albums",
            { response ->
                Log.d("tagb", response)
                val resp = JSONArray(response)
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
                cont.resume(list)
            },
            {
                cont.resumeWithException(it)
            }))
    }

    suspend fun getArtistPrizes(artistId: Int) = suspendCoroutine<List<Prize>> { cont ->
        val list = mutableListOf<Prize>()
        requestQueue.add(
            getRequest("musicians/$artistId/",
                { response ->
                    Log.d("tagb", response)
                    val resp = JSONObject(response)

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
                    cont.resume(list)
                },
                {
                    cont.resumeWithException(it)
                })
        )
    }

    suspend fun getPrize(prizeId: Int) = suspendCoroutine<Prize> { cont ->
        var prize: Prize? = null
        requestQueue.add(getRequest("prizes/$prizeId", { response ->
            Log.d("getPrize $prizeId ", response)
            val resp = JSONObject(response)

            prize = Prize(
                id = prizeId,
                name = resp.getString("name"),
                organization = resp.getString("organization"),
                description = resp.getString("description")
            )

            cont.resume(prize!!)
        }, {
            cont.resumeWithException(it)
        }))
    }

    suspend fun addAlbum(album: Album) = suspendCoroutine<Album> { cont ->
        requestQueue.add(
            postRequest("albums",
                JSONObject(
                    """{"name":"${album.name}",
                    |"cover":"${album.cover}",
                    |"releaseDate":"${album.releaseDate}",
                    |"description":"${album.description}",
                    |"genre":"${album.genre}",
                    |"recordLabel":"${album.recordLabel}"}""".trimMargin()
                ),
                { response ->
                    val albumCreated = Album(
                        id = response.getInt("id"),
                        name = response.getString("name"),
                        cover = response.getString("cover"),
                        releaseDate = response.getString("releaseDate"),
                        description = response.getString("description"),
                        genre = response.getString("genre"),
                        recordLabel = response.getString("recordLabel")
                    )

                    cont.resume(albumCreated)
                },
                {
                    cont.resumeWithException(it)
                })
        )
    }

    suspend fun getAlbum(albumId: Int) = suspendCoroutine<Album> { cont ->
        var album: Album? = null
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

                album = Album(
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

                cont.resume(album!!)
            },
            {
                cont.resumeWithException(it)
            }))
    }

    suspend fun getCollectors() = suspendCoroutine<List<Collector>> { cont ->
        val list = mutableListOf<Collector>()
        requestQueue.add(
            getRequest("collectors",
                { response ->
                    val resp = JSONArray(response)
                    for (i in 0 until resp.length()) {
                        val item = resp.getJSONObject(i)
                        list.add(
                            i, Collector(
                                collectorId = item.getInt("id"),
                                name = item.getString("name"),
                                telephone = item.getString("telephone"),
                                email = item.getString("email")
                            )
                        )
                    }
                    cont.resume(list)
                },
                {
                    cont.resumeWithException(it)
                })
        )
    }

    suspend fun getCollector(collectorId: Int) = suspendCoroutine<Collector> { cont ->
        var collector: Collector? = null
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

                collector = Collector(
                    collectorId = resp.getInt("id"),
                    name = resp.getString("name"),
                    telephone = resp.getString("telephone"),
                    email = resp.getString("email"),
                    comments = comments,
                    favoritePerformers = favPerformers,
                    collectorAlbums = albums
                )

                cont.resume(collector!!)
            },
            {
                cont.resumeWithException(it)
            }))
    }

    suspend fun getCollectorAlbums(collectorId: Int) = suspendCoroutine<List<Album>> { cont ->
        val list = mutableListOf<Album>()
        requestQueue.add(
            getRequest("collectors/$collectorId/albums",
                { response ->
                    Log.d("tagb", response)
                    val resp = JSONArray(response)
                    for (i in 0 until resp.length()) {
                        val item = resp.getJSONObject(i)

                        if (item.has("album")) {
                            val albumItem = item.getJSONObject("album")
                            list.add(
                                i,
                                Album(
                                    id = albumItem.getInt("id"),
                                    name = albumItem.getString("name"),
                                    price = item.getString("price"),
                                    status = item.getString("status"),
                                    cover = albumItem.getString("cover"),
                                    description = albumItem.getString("description"),
                                    genre = albumItem.getString("genre"),
                                    recordLabel = albumItem.getString("recordLabel"),
                                    releaseDate = albumItem.getString("releaseDate")
                                )
                            )
                        }
                    }
                    cont.resume(list)
                },
                {
                    cont.resumeWithException(it)
                })
        )
    }

    suspend fun addTrack(albumId: Int, track: Track) = suspendCoroutine<Track> { cont ->
        requestQueue.add(
            postRequest("albums/$albumId/tracks", JSONObject("""{"name":"${track.name}", "duration":"${track.duration}"}"""),
                { response ->
                    val trackCreated = Track(
                        name = response.getString("name"),
                        duration = response.getString("duration")
                    )

                    cont.resume(trackCreated)
                },
                {
                    cont.resumeWithException(it)
                })
        )
    }

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