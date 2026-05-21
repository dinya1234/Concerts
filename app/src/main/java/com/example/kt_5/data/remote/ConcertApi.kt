package com.example.kt_5.data.remote

import com.example.kt_5.data.remote.dto.ConcertDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ConcertApi {
    @GET("6b21056c3371505aafba/concerts")
    suspend fun getAllConcerts(): List<ConcertDto>

    @GET("6b21056c3371505aafba/concerts/{id}")
    suspend fun getConcertById(@Path("id") concertId: Int): ConcertDto
}