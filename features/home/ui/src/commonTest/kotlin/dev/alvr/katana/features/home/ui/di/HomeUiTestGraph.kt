package dev.alvr.katana.features.home.ui.di

import dev.alvr.katana.common.session.domain.usecases.ObserveActiveSessionUseCase
import dev.alvr.katana.common.session.domain.usecases.SaveSessionUseCase
import dev.alvr.katana.common.user.domain.usecases.SaveUserIdUseCase
import dev.alvr.katana.core.ui.navigation.DeepLinkState
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
    val homeViewModel: HomeViewModel

    @Provides
    fun homeViewModel(
        deepLinkState: DeepLinkState,
        observeActiveSessionUseCase: ObserveActiveSessionUseCase,
        saveSessionUseCase: SaveSessionUseCase,
        saveUserIdUseCase: SaveUserIdUseCase,
    ): HomeViewModel = HomeViewModel(deepLinkState, observeActiveSessionUseCase, saveSessionUseCase, saveUserIdUseCase)

    @DependencyGraph.Factory
    interface Factory {
        fun create(
            @Provides deepLinkState: DeepLinkState,
            @Provides observeActiveSessionUseCase: ObserveActiveSessionUseCase,
            @Provides saveSessionUseCase: SaveSessionUseCase,
            @Provides saveUserIdUseCase: SaveUserIdUseCase,
        ): HomeUiTestGraph
    }
}

internal fun createHomeUiTestGraph(
    deepLinkState: DeepLinkState,
    observeActiveSessionUseCase: ObserveActiveSessionUseCase,
    saveSessionUseCase: SaveSessionUseCase,
    saveUserIdUseCase: SaveUserIdUseCase,
) =
    createGraphFactory<HomeUiTestGraph.Factory>()
        .create(
            deepLinkState = deepLinkState,
            observeActiveSessionUseCase = observeActiveSessionUseCase,
            saveSessionUseCase = saveSessionUseCase,
            saveUserIdUseCase = saveUserIdUseCase,
        )
