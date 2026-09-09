package com.example.movieapp.core.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/** Manages Media3 offline downloads and exposes their state to the UI. */
@OptIn(UnstableApi::class)
class DownloadTracker(
    context: Context,
    maxCacheBytes: Long = DEFAULT_MAX_CACHE_BYTES,
    executor: Executor = Executors.newFixedThreadPool(2),
) {
    private val applicationContext = context.applicationContext
    private val databaseProvider = StandaloneDatabaseProvider(applicationContext)
    private val cache = SimpleCache(
        File(applicationContext.filesDir, DOWNLOAD_CACHE_DIRECTORY),
        LeastRecentlyUsedCacheEvictor(maxCacheBytes),
        databaseProvider,
    )
    private val downloadManager = DownloadManager(
        applicationContext,
        databaseProvider,
        cache,
        CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory()),
        executor,
    )
    private val _downloads = MutableStateFlow<List<Download>>(emptyList())

    /** Emits all known downloads whenever Media3 reports a state change. */
    val downloads: Flow<List<Download>> = _downloads.asStateFlow()

    init {
        downloadManager.addListener(object : DownloadManager.Listener {
            override fun onInitialized(downloadManager: DownloadManager) = publishDownloads()
            override fun onDownloadChanged(
                downloadManager: DownloadManager,
                download: Download,
                finalException: Exception?,
            ) = publishDownloads()
            override fun onDownloadRemoved(downloadManager: DownloadManager, download: Download) {
                publishDownloads()
            }
        })
        publishDownloads()
    }

    /** Adds or resumes a media item download. */
    fun startDownload(mediaItem: MediaItem) {
        downloadManager.addDownload(
            DownloadRequest.Builder(mediaItem.mediaId.ifBlank { mediaItem.localConfiguration?.uri.toString() }, mediaItem.localConfiguration?.uri ?: android.net.Uri.EMPTY)
                .setMimeType(mediaItem.localConfiguration?.mimeType)
                .build(),
        )
    }

    /** Pauses a download by its Media3 download ID. */
    fun pauseDownload(id: String) = downloadManager.setStopReason(id, STOP_REASON_PAUSED)

    /** Removes a download and its cached data by ID. */
    fun removeDownload(id: String) = downloadManager.removeDownload(id)

    /** Releases download manager resources and closes the cache. */
    fun release() {
        downloadManager.release()
        cache.release()
        databaseProvider.close()
    }

    private fun publishDownloads() {
        val cursor = downloadManager.downloadIndex.getDownloads()
        cursor.use {
            val result = buildList {
                while (it.moveToNext()) add(it.download)
            }
            _downloads.value = result
        }
    }

    private companion object {
        const val DOWNLOAD_CACHE_DIRECTORY = "media_downloads"
        const val DEFAULT_MAX_CACHE_BYTES = 2L * 1024 * 1024 * 1024
        const val STOP_REASON_PAUSED = 1
    }
}
