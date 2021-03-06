package com.example.proyectomoviles.models

data class Album(
    val id:Int = 0,
    val name:String,
    val cover:String,
    val releaseDate:String,
    val description:String,
    val genre:String,
    val recordLabel:String,
    val price: String = "",
    val status: String = "",
    val comments: List<Comment> = mutableListOf<Comment>(),
    val performers: List<Performer> = mutableListOf<Performer>(),
    val tracks: List<Track> = mutableListOf<Track>()
){
    val artistaRepresentation : String
        get() {
            var resp = ""
            if ( performers.isNotEmpty()) {
                resp = performers[0].name

                if (performers.size > 1) {
                    resp += ",..,"
                    resp += performers.last().name
                }
            }
            return resp
        }

    val formattedReleaseDate : String
        get() {
            var resp = ""
            if ( releaseDate.isNotBlank()) {
                resp = releaseDate.substring(0, 10)
            }
            return resp
        }
}

