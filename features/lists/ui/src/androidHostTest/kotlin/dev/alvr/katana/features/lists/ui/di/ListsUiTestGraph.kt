package dev.alvr.katana.features.lists.ui.di

import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.alvr.katana.features.lists.domain.usecases.ObserveAnimeListUseCase
import dev.alvr.katana.features.lists.domain.usecases.ObserveMangaListUseCase
import dev.alvr.katana.features.lists.domain.usecases.UpdateListUseCase
import dev.alvr.katana.features.lists.ui.viewmodel.AnimeListsViewModel
import dev.alvr.katana.features.lists.ui.viewmodel.MangaListsViewModel
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface ListsUiTestGraph : TestAppGraph {
    val animeListsViewModel: AnimeListsViewModel
    val mangaListsViewModel: MangaListsViewModel

    @Provides
    fun animeListsViewModel(
        dispatcher: KatanaDispatcher,
        updateListUseCase: UpdateListUseCase,
        observeAnimeListUseCase: ObserveAnimeListUseCase,
    ): AnimeListsViewModel = AnimeListsViewModel(dispatcher, updateListUseCase, observeAnimeListUseCase)

    @Provides
    fun mangaListsViewModel(
        dispatcher: KatanaDispatcher,
        updateListUseCase: UpdateListUseCase,
        observeMangaListUseCase: ObserveMangaListUseCase,
    ): MangaListsViewModel = MangaListsViewModel(dispatcher, updateListUseCase, observeMangaListUseCase)

    @DependencyGraph.Factory
    interface Factory {
        fun create(
            @Provides updateListUseCase: UpdateListUseCase,
            @Provides observeAnimeListUseCase: ObserveAnimeListUseCase,
            @Provides observeMangaListUseCase: ObserveMangaListUseCase,
        ): ListsUiTestGraph
    }
}

internal fun createListsUiTestGraph(
    updateListUseCase: UpdateListUseCase,
    observeAnimeListUseCase: ObserveAnimeListUseCase,
    observeMangaListUseCase: ObserveMangaListUseCase,
) =
    createGraphFactory<ListsUiTestGraph.Factory>()
        .create(updateListUseCase, observeAnimeListUseCase, observeMangaListUseCase)
