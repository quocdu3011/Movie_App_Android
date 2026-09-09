package com.example.movieapp.core.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.drm.DefaultDrmSessionManager
import androidx.media3.exoplayer.drm.DrmSessionManager
import androidx.media3.exoplayer.drm.DrmSessionManagerProvider
import androidx.media3.exoplayer.drm.HttpMediaDrmCallback
import androidx.media3.exoplayer.drm.FrameworkMediaDrm

/** Creates Widevine DRM session managers for signed license endpoints. */
@OptIn(UnstableApi::class)
class WidevineDrmSessionManagerProvider(
    context: Context,
    private val accessToken: String? = null,
) : DrmSessionManagerProvider {
    private val applicationContext = context.applicationContext
    private val dataSourceFactory = DefaultHttpDataSource.Factory()

    override fun get(mediaItem: androidx.media3.common.MediaItem): DrmSessionManager {
        val licenseUrl = mediaItem.localConfiguration?.drmConfiguration?.licenseUri?.toString()
            ?: error("Widevine media item is missing a license URL")
        return create(licenseUrl)
    }

    /** Creates a Widevine session manager for the supplied license URL. */
    fun create(licenseUrl: String): DrmSessionManager {
        val callback = HttpMediaDrmCallback(licenseUrl, dataSourceFactory).apply {
            setKeyRequestProperty("Content-Type", "application/octet-stream")
            accessToken?.takeIf(String::isNotBlank)?.let {
                setKeyRequestProperty("Authorization", "Bearer $it")
            }
        }
        return DefaultDrmSessionManager.Builder()
            .setUuidAndExoMediaDrmProvider(C.WIDEVINE_UUID, FrameworkMediaDrm.DEFAULT_PROVIDER)
            .build(callback)
    }
}
