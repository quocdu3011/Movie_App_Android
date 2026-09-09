package com.example.movieapp.core.database.entity

import androidx.room.Entity

/** Stores the fields needed to render a cached movie while offline or loading. */
@Entity(tableName = "cached_movies")
data class CachedMovieEntity(
    @androidx.room.PrimaryKey val id: String,
    val title: String,
    val posterUrl: String,
    val backdropUrl: String,
    val genres: String,
    val cachedAt: Long,
)
