package com.example.movieapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movieapp.core.database.dao.CachedMovieDao
import com.example.movieapp.core.database.dao.FavoriteDao
import com.example.movieapp.core.database.dao.WatchHistoryDao
import com.example.movieapp.core.database.entity.CachedMovieEntity
import com.example.movieapp.core.database.entity.FavoriteEntity
import com.example.movieapp.core.database.entity.WatchHistoryEntity

/** Room database containing local movie cache and user playback data. */
@Database(
    entities = [
        WatchHistoryEntity::class,
        FavoriteEntity::class,
        CachedMovieEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    /** DAO for playback progress. */
    abstract fun watchHistoryDao(): WatchHistoryDao

    /** DAO for favorites. */
    abstract fun favoriteDao(): FavoriteDao

    /** DAO for cached movies. */
    abstract fun cachedMovieDao(): CachedMovieDao
}
