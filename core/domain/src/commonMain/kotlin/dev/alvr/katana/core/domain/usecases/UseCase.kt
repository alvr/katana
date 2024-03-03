package dev.alvr.katana.core.domain.usecases

import arrow.core.Either
import arrow.core.Option
import dev.alvr.katana.core.domain.failures.Failure
import kotlinx.coroutines.runBlocking

sealed interface UseCase<in P, out R> : suspend (P) -> R {
    fun sync(params: P): R = runBlocking {
        invoke(params)
    }
}

interface EitherUseCase<in P, out R> : UseCase<P, Either<Failure, R>>
interface OptionUseCase<in P, out R> : UseCase<P, Option<R>>

suspend operator fun <R> UseCase<Unit, R>.invoke(): R = this(Unit)
fun <R> UseCase<Unit, R>.sync(): R = sync(Unit)
