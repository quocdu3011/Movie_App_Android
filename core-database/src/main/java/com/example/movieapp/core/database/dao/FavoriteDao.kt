package com.example.movieapp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.movieapp.core.database.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

/** Provides persistence operations for the user's favorite movies. */
@Dao
interface FavoriteDao {
    /** Observes all favorites, newest first. */
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    /** Observes one favorite by movie ID. */
    @Query("SELECT * FROM favorites WHERE movieId = :movieId LIMIT 1")
    fun observe(movieId: String): Flow<FavoriteEntity?>

    /** Finds one favorite by movie ID. */
    @Query("SELECT * FROM favorites WHERE movieId = :movieId LIMIT 1")
    suspend fun find(movieId: String): FavoriteEntity?

    /** Inserts or updates a favorite. */
    @Upsert
    suspend fun upsert(favorite: FavoriteEntity)

    /** Deletes a favorite. */
    @Delete
    suspend fun delete(favorite: FavoriteEntity)

    /** Deletes a favorite by movie ID. */
    @Query("DELETE FROM favorites WHERE movieId = :movieId")
    suspend fun deleteByMovieId(movieId: String)

    /** Deletes all favorites. */
    @Query("DELETE FROM favorites")
    suspend fun deleteAll()
}
