package dev.alvr.katana.core.domain.usecases

import arrow.core.Either
import arrow.core.Option
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.failures.Failure
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

abstract class UseCase<in P, out R> internal constructor(private val dispatcher: KatanaDispatcher) {

    abstract suspend fun run(params: P): R

    suspend operator fun invoke(params: P): R = withContext(dispatcher.io) {
        run(params)
    }

    fun sync(params: P): R = runBlocking {
        invoke(params)
    }
}

abstract class EitherUseCase<in P, out R>(dispatcher: KatanaDispatcher) :
    UseCase<P, Either<Failure, R>>(dispatcher)

abstract class OptionUseCase<in P, out R>(dispatcher: KatanaDispatcher) :
    UseCase<P, Option<R>>(dispatcher)

suspend operator fun <R> UseCase<Unit, R>.invoke(): R = invoke(Unit)
fun <R> UseCase<Unit, R>.sync(): R = sync(Unit)
