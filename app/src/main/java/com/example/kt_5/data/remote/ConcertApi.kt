package com.example.kt_5.data.remote

import com.example.kt_5.data.remote.dto.ConcertDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ConcertApi {
    @GET("7f92b42e55fc57670ab4/concerts")
    suspend fun getAllConcerts(): List<ConcertDto>

    @GET("7f92b42e55fc57670ab4/concerts/{id}")
    suspend fun getConcertById(@Path("id") concertId: Int): ConcertDto
}