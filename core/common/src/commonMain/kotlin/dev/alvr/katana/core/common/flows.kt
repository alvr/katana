package dev.alvr.katana.core.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

fun <T> Flow<T>.onSubscribe(block: () -> Unit) =
    object : Flow<T> by this {
        override suspend fun collect(collector: FlowCollector<T>) {
            block()
            this@onSubscribe.collect(collector)
        }
    }
