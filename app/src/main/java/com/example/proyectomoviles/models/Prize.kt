package com.example.proyectomoviles.models

data class Prize (
    val id: Int,
    val organization: String,
    val name: String,
    val description: String,
    val premiationDate: String = ""
) {
    val formattedPremiationDate : String
        get() {
            var resp = ""
            if ( premiationDate.isNotBlank()) {
                resp = premiationDate.substring(0, 10)
            }
            return resp
        }
}
