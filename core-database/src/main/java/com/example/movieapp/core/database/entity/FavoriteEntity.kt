package com.example.movieapp.core.database.entity

import androidx.room.Entity

/** Stores a movie saved by the user. */
@Entity(tableName = "favorites")
data class FavoriteEntity(
    @androidx.room.PrimaryKey val movieId: String,
    val title: String,
    val posterUrl: String,
    val addedAt: Long,
)
