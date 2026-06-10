package com.example.kt_5.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kt_5.data.local.dao.ConcertDao
import com.example.kt_5.data.local.dao.TicketDao
import com.example.kt_5.data.local.entity.ConcertEntity
import com.example.kt_5.data.local.entity.TicketEntity

@Database(entities = [ConcertEntity::class, TicketEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun concertDao(): ConcertDao
    abstract fun ticketDao(): TicketDao
}