package com.example.movieapp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.movieapp.core.database.entity.CachedMovieEntity
import kotlinx.coroutines.flow.Flow

/** Provides persistence operations for cached movie summaries. */
@Dao
interface CachedMovieDao {
    /** Observes all cached movies, newest first. */
    @Query("SELECT * FROM cached_movies ORDER BY cachedAt DESC")
    fun getAllMovies(): Flow<List<CachedMovieEntity>>

    /** Observes one cached movie by ID. */
    @Query("SELECT * FROM cached_movies WHERE id = :movieId LIMIT 1")
    fun observe(movieId: String): Flow<CachedMovieEntity?>

    /** Finds one cached movie by ID. */
    @Query("SELECT * FROM cached_movies WHERE id = :movieId LIMIT 1")
    suspend fun find(movieId: String): CachedMovieEntity?

    /** Inserts or updates a cached movie. */
    @Upsert
    suspend fun upsert(movie: CachedMovieEntity)

    /** Inserts or updates multiple cached movies. */
    @Upsert
    suspend fun upsertAll(movies: List<CachedMovieEntity>)

    /** Deletes one cached movie. */
    @Delete
    suspend fun delete(movie: CachedMovieEntity)

    /** Deletes all cached movies. */
    @Query("DELETE FROM cached_movies")
    suspend fun deleteAll()
}
