package dev.alvr.katana.domain.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

abstract class UseCase<in P, out R>(private val dispatcher: CoroutineDispatcher) {

    protected abstract suspend fun doWork(params: P): R

    suspend operator fun invoke(params: P): R = withContext(dispatcher) {
        doWork(params)
    }

    fun sync(params: P): R = runBlocking(dispatcher) {
        doWork(params)
    }
}

suspend operator fun <R> UseCase<Unit, R>.invoke(): R = this(Unit)
fun <R> UseCase<Unit, R>.sync(): R = sync(Unit)
