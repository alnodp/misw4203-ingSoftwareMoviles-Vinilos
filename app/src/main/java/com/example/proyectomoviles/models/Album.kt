package com.example.proyectomoviles.models

import androidx.room.Entity

data class Album(
    val id:Int,
    val name:String,
    val cover:String,
    val releaseDate:String,
    val description:String,
    val genre:String,
    val recordLabel:String,
    val performers: List<Performer> = mutableListOf<Performer>()
    // TODO tracks
    // TODO comments
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
}

