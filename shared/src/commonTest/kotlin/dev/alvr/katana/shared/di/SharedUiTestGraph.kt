package dev.alvr.katana.shared.di

import dev.alvr.katana.common.session.domain.usecases.ObserveActiveSessionUseCase
import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.alvr.katana.core.ui.viewmodel.EmptyEffect
import dev.alvr.katana.core.ui.viewmodel.EmptyIntent
import dev.alvr.katana.core.ui.viewmodel.KatanaViewModel as KatanaBaseViewModel
import dev.alvr.katana.shared.viewmodel.KatanaState
import dev.alvr.katana.shared.viewmodel.KatanaViewModel
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

private typealias KatanaVM = KatanaBaseViewModel<KatanaState, EmptyEffect, EmptyIntent>

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface SharedUiTestGraph : TestAppGraph {
    val katanaViewModel: KatanaVM

    @Provides
    fun katanaViewModel(observeActiveSessionUseCase: ObserveActiveSessionUseCase): KatanaVM =
        KatanaViewModel(observeActiveSessionUseCase)

    @DependencyGraph.Factory
    interface Factory {
        fun create(@Provides observeActiveSessionUseCase: ObserveActiveSessionUseCase): SharedUiTestGraph
    }
}

internal fun createSharedUiTestGraph(observeActiveSessionUseCase: ObserveActiveSessionUseCase) =
    createGraphFactory<SharedUiTestGraph.Factory>().create(observeActiveSessionUseCase)
