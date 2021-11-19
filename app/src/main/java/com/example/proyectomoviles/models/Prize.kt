package com.example.proyectomoviles.models

data class Prize (
    val id: Int,
    var organization: String = "",
    var name: String = "",
    var description: String = "",
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
