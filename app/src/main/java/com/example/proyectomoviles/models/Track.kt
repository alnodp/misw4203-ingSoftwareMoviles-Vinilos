package com.example.proyectomoviles.models

data class Track (
    val id: Int,
    val name: String,
    val duration: String,
    val minutes: String = "",
    val seconds: String = ""
)