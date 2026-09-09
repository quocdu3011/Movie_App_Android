package com.example.movieapp.core.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/** Provides coroutine dispatchers so production code can be replaced in tests. */
interface DispatcherProvider {
    /** Dispatcher for blocking I/O work. */
    val io: CoroutineDispatcher

    /** Dispatcher associated with the main/UI thread. */
    val main: CoroutineDispatcher

    /** Dispatcher for CPU-intensive work. */
    val default: CoroutineDispatcher
}

/** Uses the standard Kotlin coroutine dispatchers. */
object DefaultDispatcherProvider : DispatcherProvider {
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val default: CoroutineDispatcher = Dispatchers.Default
}
