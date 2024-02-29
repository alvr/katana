package dev.alvr.katana.domain.base.usecases

import arrow.core.Either
import arrow.core.Option
import dev.alvr.katana.domain.base.failures.Failure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest

sealed interface FlowUseCase<in P, out R> : (P) -> Unit {
    val flow: Flow<R>
}

operator fun <R> FlowUseCase<Unit, R>.invoke() {
    invoke(Unit)
}

@OptIn(ExperimentalCoroutinesApi::class)
sealed class BaseFlowUseCase<in P, out R> : FlowUseCase<P, R> {
    private val paramState = MutableSharedFlow<P>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override val flow: Flow<R> = paramState.flatMapLatest {
        createFlow(it).distinctUntilChanged()
    }

    protected abstract fun createFlow(params: P): Flow<R>

    override operator fun invoke(params: P) {
        paramState.tryEmit(params)
    }
}

abstract class FlowEitherUseCase<in P, out R> : BaseFlowUseCase<P, Either<Failure, R>>()
abstract class FlowOptionUseCase<in P, out R> : BaseFlowUseCase<P, Option<R>>()
