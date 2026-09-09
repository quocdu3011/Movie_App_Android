package com.example.movieapp.core.common

/**
 * Represents the outcome of an operation that can either succeed or fail.
 */
sealed interface Result<out T> {
    /** A successful operation containing its resulting data. */
    data class Success<T>(val data: T) : Result<T>

    /** A failed operation with a user-facing message and an optional cause. */
    data class Error(
        val message: String,
        val throwable: Throwable? = null,
    ) : Result<Nothing>
}

/** Transforms successful data while preserving an error unchanged. */
inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.Success(transform(data))
    is Result.Error -> this
}

/** Folds either branch into a single value. */
inline fun <T, R> Result<T>.fold(
    onSuccess: (T) -> R,
    onError: (Result.Error) -> R,
): R = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Error -> onError(this)
}
