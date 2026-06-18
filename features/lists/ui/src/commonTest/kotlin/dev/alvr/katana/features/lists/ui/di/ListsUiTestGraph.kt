package dev.alvr.katana.features.lists.ui.di

import dev.alvr.katana.common.media.domain.usecases.ObserveMediaCollectionUseCase
import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
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
        fun create(
            @Provides observeListUseCase: ObserveMediaCollectionUseCase,
            @Provides updateListUseCase: UpdateListUseCase,
        ): ListsUiTestGraph
    }
}

internal fun createListsUiTestGraph(
    observeListUseCase: ObserveMediaCollectionUseCase,
    updateListUseCase: UpdateListUseCase,
) =
    createGraphFactory<ListsUiTestGraph.Factory>()
        .create(observeListUseCase = observeListUseCase, updateListUseCase = updateListUseCase)
