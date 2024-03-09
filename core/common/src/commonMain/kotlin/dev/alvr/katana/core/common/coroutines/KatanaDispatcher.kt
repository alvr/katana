package dev.alvr.katana.core.common.coroutines

import kotlin.jvm.JvmField
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.MainCoroutineDispatcher

@Suppress("UseDataClass")
class KatanaDispatcher internal constructor(
    @JvmField val main: MainCoroutineDispatcher = Dispatchers.Main,
    @JvmField val io: CoroutineDispatcher = Dispatchers.IO,
    @JvmField val default: CoroutineDispatcher = Dispatchers.Default,
)
