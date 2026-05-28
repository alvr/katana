package dev.alvr.katana.core.domain.usecases

import arrow.core.Either
import arrow.core.Option
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.failures.Failure
import kotlinx.coroutines.withContext

interface KatanaUseCase<in P, out R> {
    /**
 * Executes the use case with the given parameters.
 *
 * @param params Input required to perform the use case.
 * @return The result produced by the use case.
 */
suspend operator fun invoke(params: P): R
}

abstract class UseCase<in P, out R> internal constructor(private val dispatcher: KatanaDispatcher) :
    KatanaUseCase<P, R> {

    /**
 * Executes the use case logic for the provided parameters and yields its result.
 *
 * @param params Input parameters for the use case.
 * @return The result produced by this use case.
 */
protected abstract suspend fun run(params: P): R

    /**
 * Invokes the use case with the given parameters, executing its work on the configured IO dispatcher.
 *
 * @param params Parameters required to run the use case.
 * @return The result produced by the use case.
 */
override suspend operator fun invoke(params: P): R = withContext(dispatcher.io) { run(params) }
}

interface KatanaEitherUseCase<in P, out R> : KatanaUseCase<P, Either<Failure, R>>

abstract class EitherUseCase<in P, out R>(dispatcher: KatanaDispatcher) :
    UseCase<P, Either<Failure, R>>(dispatcher), KatanaEitherUseCase<P, R>

interface KatanaOptionUseCase<in P, out R> : KatanaUseCase<P, Option<R>>

abstract class OptionUseCase<in P, out R>(dispatcher: KatanaDispatcher) :
    UseCase<P, Option<R>>(dispatcher), KatanaOptionUseCase<P, R>

/**
 * Invokes a parameterless use case.
 *
 * @return The result produced by the use case execution.
 */
suspend operator fun <R> KatanaUseCase<Unit, R>.invoke(): R = invoke(Unit)
