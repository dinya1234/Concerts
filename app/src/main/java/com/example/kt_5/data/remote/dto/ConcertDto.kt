package com.example.kt_5.data.remote.dto


import com.example.kt_5.data.local.entity.ConcertEntity

data class ConcertDto(
    val id: Int,
    val artist: String,
    val venue: String,
    val date: String,
    val city: String,
    val description: String,
    val price: Double
)

fun ConcertDto.toEntity(isFavorite: Boolean = false) = ConcertEntity(
    id, artist, venue, date, city, description, price, isFavorite
)