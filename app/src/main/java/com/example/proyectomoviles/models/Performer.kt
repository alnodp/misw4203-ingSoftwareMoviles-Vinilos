package com.example.proyectomoviles.models

data class Performer(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String
) {
    val formattedBirthDate : String
        get() {
            var resp = ""
            if (birthDate.isNotBlank()) {
                resp = birthDate.substring(0, 10)
            }
            return resp
        }
}