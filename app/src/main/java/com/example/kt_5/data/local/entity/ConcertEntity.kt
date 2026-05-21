package com.example.kt_5.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "concerts")
data class ConcertEntity(
    @PrimaryKey val id: Int,
    val artist: String,
    val venue: String,
    val date: String,
    val city: String,
    val description: String,
    val price: Double
)