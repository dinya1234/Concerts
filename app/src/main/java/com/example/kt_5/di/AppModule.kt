package com.example.kt_5.di

import android.content.Context
import androidx.room.Room
import com.example.kt_5.data.local.AppDatabase
import com.example.kt_5.data.local.dao.ConcertDao
import com.example.kt_5.data.remote.ConcertApi
import com.example.kt_5.data.repository.ConcertRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppModule(context: Context) {

    private val BASE_URL = "https://api.npoint.io/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ConcertApi = retrofit.create(ConcertApi::class.java)

    private val database = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "concerts_db"
    ).fallbackToDestructiveMigration().build()

    val dao: ConcertDao = database.concertDao()

    val repository = ConcertRepository(api, dao)
}