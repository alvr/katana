package dev.alvr.katana.core.common.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface KatanaDispatcher {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}
