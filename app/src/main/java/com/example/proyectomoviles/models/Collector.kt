package com.example.proyectomoviles.models

data class Collector(
    val collectorId: Int,
    val name: String,
    val telephone: String,
    val email: String,
    val comments: List<Comment> = mutableListOf<Comment>(),
    val favoritePerformers: List<Performer> = mutableListOf<Performer>(),
    val collectorAlbums: List<Album> = mutableListOf<Album>()
)