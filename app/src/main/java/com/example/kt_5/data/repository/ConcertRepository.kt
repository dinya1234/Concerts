package com.example.kt_5.data.repository

import com.example.kt_5.data.local.dao.ConcertDao
import com.example.kt_5.data.local.entity.ConcertEntity
import com.example.kt_5.data.remote.ConcertApi
import com.example.kt_5.data.remote.dto.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ConcertRepository(
    private val api: ConcertApi,
    private val dao: ConcertDao
) {
    fun getAllConcerts(): Flow<List<ConcertEntity>> = dao.getAllConcerts()

    suspend fun getConcertById(id: Int): ConcertEntity? = dao.getConcertById(id)

    suspend fun refreshConcerts(): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                val dtos = api.getAllConcerts()
                val entities = dtos.map { it.toEntity() }
                dao.deleteAllConcerts()
                dao.insertAllConcerts(entities)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}