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

interface KatanaFlowUseCase<in P, out R> : KatanaUseCase<P, Unit> {
    val flow: Flow<R>

    override suspend operator fun invoke(params: P)
}

@OptIn(ExperimentalCoroutinesApi::class)
abstract class FlowUseCase<in P, out R> internal constructor(private val dispatcher: KatanaDispatcher) :
    KatanaFlowUseCase<P, R> {
    private val paramState = MutableSharedFlow<P>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override val flow: Flow<R> =
        paramState.distinctUntilChanged().flatMapLatest { params ->
            createFlow(params).flowOn(dispatcher.io).distinctUntilChanged()
        }

    protected abstract fun createFlow(params: P): Flow<R>

    override suspend operator fun invoke(params: P) {
        paramState.emit(params)
    }
}

interface KatanaFlowEitherUseCase<in P, out R> : KatanaFlowUseCase<P, Either<Failure, R>>

abstract class FlowEitherUseCase<in P, out R>(dispatcher: KatanaDispatcher) :
    FlowUseCase<P, Either<Failure, R>>(dispatcher), KatanaFlowEitherUseCase<P, R>

interface KatanaFlowOptionUseCase<in P, out R> : KatanaFlowUseCase<P, Option<R>>

abstract class FlowOptionUseCase<in P, out R>(dispatcher: KatanaDispatcher) :
    FlowUseCase<P, Option<R>>(dispatcher), KatanaFlowOptionUseCase<P, R>

suspend operator fun <R> KatanaFlowUseCase<Unit, R>.invoke() {
    invoke(Unit)
}
