package dev.alvr.katana.features.home.ui.di

import dev.alvr.katana.common.session.domain.usecases.ObserveActiveSessionUseCase
import dev.alvr.katana.common.session.domain.usecases.SaveSessionUseCase
import dev.alvr.katana.common.user.domain.usecases.SaveUserIdUseCase
import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.alvr.katana.features.home.ui.viewmodel.HomeViewModel
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface HomeUiTestGraph : TestAppGraph {
    val homeViewModelFactory: HomeViewModel.Factory

    @DependencyGraph.Factory
    interface Factory {
        /**
         * Create a HomeUiTestGraph configured with the provided session and user persistence use cases.
         *
         * @param observeActiveSessionUseCase Observes the currently active session state for tests.
         * @param saveSessionUseCase Persists session data for tests.
         * @param saveUserIdUseCase Persists the user ID for tests.
         * @return The constructed HomeUiTestGraph instance.
         */
        fun create(
            @Provides observeActiveSessionUseCase: ObserveActiveSessionUseCase,
            @Provides saveSessionUseCase: SaveSessionUseCase,
            @Provides saveUserIdUseCase: SaveUserIdUseCase,
        ): HomeUiTestGraph
    }
}

/**
         * Create a Home UI test dependency graph configured with the provided session and user persistence use cases.
         *
         * @param observeActiveSessionUseCase Use case that observes the active session; supplied to the graph.
         * @param saveSessionUseCase Use case that persists session data; supplied to the graph.
         * @param saveUserIdUseCase Use case that persists the current user ID; supplied to the graph.
         * @return The constructed HomeUiTestGraph configured for tests.
         */
        internal fun createHomeUiTestGraph(
    observeActiveSessionUseCase: ObserveActiveSessionUseCase,
    saveSessionUseCase: SaveSessionUseCase,
    saveUserIdUseCase: SaveUserIdUseCase,
) =
    createGraphFactory<HomeUiTestGraph.Factory>()
        .create(
            observeActiveSessionUseCase = observeActiveSessionUseCase,
            saveSessionUseCase = saveSessionUseCase,
            saveUserIdUseCase = saveUserIdUseCase,
        )
