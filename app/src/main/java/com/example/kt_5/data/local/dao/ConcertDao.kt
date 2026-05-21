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

    @Query("SELECT * FROM concerts WHERE id = :id")
    suspend fun getConcertById(id: Int): ConcertEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllConcerts(concerts: List<ConcertEntity>)

    @Query("DELETE FROM concerts")
    suspend fun deleteAllConcerts()
}