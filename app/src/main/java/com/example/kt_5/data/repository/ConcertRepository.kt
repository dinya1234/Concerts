package com.example.kt_5.data.repository

import com.example.kt_5.data.local.dao.ConcertDao
import com.example.kt_5.data.local.dao.TicketDao
import com.example.kt_5.data.local.entity.ConcertEntity
import com.example.kt_5.data.local.entity.TicketEntity
import com.example.kt_5.data.remote.ConcertApi
import com.example.kt_5.data.remote.dto.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ConcertRepository(
    private val api: ConcertApi,
    private val concertDao: ConcertDao,
    private val ticketDao: TicketDao
) {
    fun getAllConcerts(): Flow<List<ConcertEntity>> = concertDao.getAllConcerts()

    fun getConcertsByCity(city: String): Flow<List<ConcertEntity>> =
        concertDao.getConcertsByCity(city)

    fun getFavoriteConcerts(): Flow<List<ConcertEntity>> = concertDao.getFavoriteConcerts()

    fun getAllTickets(): Flow<List<TicketEntity>> = ticketDao.getAllTickets()

    suspend fun getConcertById(id: Int): ConcertEntity? = concertDao.getConcertById(id)

    suspend fun hasCachedConcerts(): Boolean = concertDao.getConcertsCount() > 0

    suspend fun toggleFavorite(id: Int, isFavorite: Boolean) {
        concertDao.updateFavorite(id, isFavorite)
    }

    suspend fun purchaseTicket(concert: ConcertEntity, email: String) {
        ticketDao.insertTicket(
            TicketEntity(
                concertId = concert.id,
                artist = concert.artist,
                venue = concert.venue,
                date = concert.date,
                city = concert.city,
                price = concert.price,
                email = email
            )
        )
    }

    suspend fun refreshConcerts(): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                val favoriteIds = concertDao.getFavoriteIds().toSet()
                val dtos = api.getAllConcerts()
                val entities = dtos.map { it.toEntity(isFavorite = it.id in favoriteIds) }
                concertDao.deleteAllConcerts()
                concertDao.insertAllConcerts(entities)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
