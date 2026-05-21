package com.example.kt_5.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kt_5.data.local.dao.ConcertDao
import com.example.kt_5.data.local.entity.ConcertEntity

@Database(entities = [ConcertEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun concertDao(): ConcertDao
}