package com.example.movieapp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import com.example.movieapp.core.database.entity.WatchHistoryEntity
import kotlinx.coroutines.flow.Flow

/** Provides persistence operations for playback progress. */
@Dao
interface WatchHistoryDao {
    /** Observes all watch history entries, newest first. */
    @Query("SELECT * FROM watch_history ORDER BY updatedAt DESC")
    fun getAll(): Flow<List<WatchHistoryEntity>>

    /** Observes progress for a specific movie and optional episode. */
    @Query("SELECT * FROM watch_history WHERE movieId = :movieId AND episodeId IS :episodeId LIMIT 1")
    fun observe(movieId: String, episodeId: String?): Flow<WatchHistoryEntity?>

    /** Finds a progress entry synchronously for a write or one-shot read. */
    @Query("SELECT * FROM watch_history WHERE movieId = :movieId AND episodeId IS :episodeId LIMIT 1")
    suspend fun find(movieId: String, episodeId: String?): WatchHistoryEntity?

    /** Inserts or updates progress for a movie/episode pair. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(history: WatchHistoryEntity): Long

    /** Upserts progress using the movie/episode pair as the logical key. */
    @Transaction
    suspend fun upsert(history: WatchHistoryEntity) {
        val existing = find(history.movieId, history.episodeId)
        insertOrReplace(history.copy(id = existing?.id ?: 0))
    }

    /** Deletes one progress entry. */
    @Delete
    suspend fun delete(history: WatchHistoryEntity)

    /** Deletes all playback progress. */
    @Query("DELETE FROM watch_history")
    suspend fun deleteAll()
}
