package dev.alvr.katana.shared.di

import dev.alvr.katana.common.session.domain.usecases.ObserveActiveSessionUseCase
import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.alvr.katana.shared.viewmodel.KatanaViewModel
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface SharedUiTestGraph : TestAppGraph {
    val katanaViewModel: KatanaViewModel

    /**
         * Provides a KatanaViewModel configured with the given ObserveActiveSessionUseCase.
         *
         * @param observeActiveSessionUseCase Observes active session state used by the view model.
         * @return A KatanaViewModel that exposes active session updates.
         */
        @Provides
    fun katanaViewModel(observeActiveSessionUseCase: ObserveActiveSessionUseCase): KatanaViewModel =
        KatanaViewModel(observeActiveSessionUseCase)

    @DependencyGraph.Factory
    interface Factory {
        fun create(@Provides observeActiveSessionUseCase: ObserveActiveSessionUseCase): SharedUiTestGraph
    }
}

internal fun createSharedUiTestGraph(observeActiveSessionUseCase: ObserveActiveSessionUseCase) =
    createGraphFactory<SharedUiTestGraph.Factory>().create(observeActiveSessionUseCase)
