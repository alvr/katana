package dev.alvr.katana.core.domain.usecases

import arrow.core.Either
import arrow.core.Option
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.failures.Failure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn

@OptIn(ExperimentalCoroutinesApi::class)
abstract class FlowUseCase<in P, out R> internal constructor(private val dispatcher: KatanaDispatcher) {
    private val paramState = MutableSharedFlow<P>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    val flow: Flow<R> =
        paramState.distinctUntilChanged().flatMapLatest { params ->
            createFlow(params).flowOn(dispatcher.io).distinctUntilChanged()
        }

    protected abstract fun createFlow(params: P): Flow<R>

    suspend operator fun invoke(params: P) {
        paramState.emit(params)
    }
}

suspend operator fun <R> FlowUseCase<Unit, R>.invoke() {
    invoke(Unit)
}

abstract class FlowEitherUseCase<in P, out R>(dispatcher: KatanaDispatcher) :
    FlowUseCase<P, Either<Failure, R>>(dispatcher)

abstract class FlowOptionUseCase<in P, out R>(dispatcher: KatanaDispatcher) : FlowUseCase<P, Option<R>>(dispatcher)
