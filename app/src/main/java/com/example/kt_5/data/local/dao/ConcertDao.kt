package com.example.kt_5.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kt_5.data.local.entity.ConcertEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ConcertDao {
    @Query("SELECT * FROM concerts ORDER BY date ASC")
    fun getAllConcerts(): Flow<List<ConcertEntity>>

    @Query("SELECT * FROM concerts WHERE city LIKE '%' || :city || '%' ORDER BY date ASC")
    fun getConcertsByCity(city: String): Flow<List<ConcertEntity>>

    @Query("SELECT * FROM concerts WHERE isFavorite = 1 ORDER BY date ASC")
    fun getFavoriteConcerts(): Flow<List<ConcertEntity>>

    @Query("SELECT id FROM concerts WHERE isFavorite = 1")
    suspend fun getFavoriteIds(): List<Int>

    @Query("SELECT * FROM concerts WHERE id = :id")
    suspend fun getConcertById(id: Int): ConcertEntity?

    @Query("SELECT COUNT(*) FROM concerts")
    suspend fun getConcertsCount(): Int

    @Query("UPDATE concerts SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllConcerts(concerts: List<ConcertEntity>)

    @Query("DELETE FROM concerts")
    suspend fun deleteAllConcerts()
}
