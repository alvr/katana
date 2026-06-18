package dev.alvr.katana.features.explore.ui.di

import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.alvr.katana.core.ui.viewmodel.EmptyEffect
import dev.alvr.katana.core.ui.viewmodel.EmptyIntent
import dev.alvr.katana.core.ui.viewmodel.EmptyState
import dev.alvr.katana.core.ui.viewmodel.KatanaViewModel
import dev.alvr.katana.features.explore.ui.viewmodel.ExploreViewModel
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

private typealias ExploreVM = KatanaViewModel<EmptyState, EmptyEffect, EmptyIntent>

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface ExploreUiTestGraph : TestAppGraph {
    val exploreViewModel: ExploreVM

    @Provides fun exploreViewModel(): ExploreVM = ExploreViewModel()

    @DependencyGraph.Factory
    interface Factory {
        fun create(): ExploreUiTestGraph
    }
}

internal fun createExploreUiTestGraph() = createGraphFactory<ExploreUiTestGraph.Factory>().create()
