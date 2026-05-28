package dev.alvr.katana.features.lists.ui.di

import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.alvr.katana.features.lists.domain.usecases.ObserveListUseCase
import dev.alvr.katana.features.lists.domain.usecases.UpdateListUseCase
import dev.alvr.katana.features.lists.ui.viewmodel.ListsViewModel
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface ListsUiTestGraph : TestAppGraph {
    val listsViewModelFactory: ListsViewModel.Factory

    @DependencyGraph.Factory
    interface Factory {
        /**
         * Creates a test dependency graph for the Lists UI using the supplied use cases.
         *
         * @param observeListUseCase Use case that provides a stream of list state for observation in tests.
         * @param updateListUseCase Use case that performs updates to list state during tests.
         * @return A configured [ListsUiTestGraph] instance scoped for Lists UI testing.
         */
        fun create(
            @Provides observeListUseCase: ObserveListUseCase,
            @Provides updateListUseCase: UpdateListUseCase,
        ): ListsUiTestGraph
    }
}

/**
         * Create a ListsUiTestGraph configured with the provided list use cases.
         *
         * @param observeListUseCase Use case that observes list state for the UI.
         * @param updateListUseCase Use case that updates list data for the UI.
         * @return A fully constructed [ListsUiTestGraph] instance wired with the given dependencies.
         */
        internal fun createListsUiTestGraph(observeListUseCase: ObserveListUseCase, updateListUseCase: UpdateListUseCase) =
    createGraphFactory<ListsUiTestGraph.Factory>()
        .create(observeListUseCase = observeListUseCase, updateListUseCase = updateListUseCase)
