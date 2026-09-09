package com.example.movieapp.core.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Formats a movie duration in minutes as a compact hour/minute label. */
fun Int.formatMovieDuration(): String {
    require(this >= 0) { "Duration must not be negative" }
    val hours = this / 60
    val minutes = this % 60
    return when {
        hours == 0 -> "${minutes}m"
        minutes == 0 -> "${hours}h"
        else -> "${hours}h ${minutes}m"
    }
}

/** Formats an instant using the supplied pattern and time zone. */
fun Instant.formatDateTime(
    pattern: String = "dd/MM/yyyy HH:mm",
    zoneId: ZoneId = ZoneId.systemDefault(),
    locale: Locale = Locale.getDefault(),
): String = DateTimeFormatter.ofPattern(pattern, locale)
    .withZone(zoneId)
    .format(this)

/** Converts upstream values and exceptions into a common [Result] flow. */
fun <T> Flow<T>.asResult(): Flow<Result<T>> = map<T, Result<T>> { Result.Success(it) }
    .catch { emit(Result.Error(it.message ?: "Đã xảy ra lỗi", it)) }
