package com.example.movieapp.core.database.entity

import androidx.room.Entity
import androidx.room.Index

/** Stores playback progress for a movie or one of its episodes. */
@Entity(
    tableName = "watch_history",
    indices = [Index(value = ["movieId", "episodeId"], unique = true)],
)
data class WatchHistoryEntity(
    @androidx.room.PrimaryKey(autoGenerate = true) val id: Long = 0,
    val movieId: String,
    val episodeId: String? = null,
    val positionSeconds: Long,
    val durationSeconds: Long,
    val updatedAt: Long,
)
