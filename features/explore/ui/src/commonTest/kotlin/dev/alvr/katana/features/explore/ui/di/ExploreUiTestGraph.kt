package dev.alvr.katana.features.explore.ui.di

import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.alvr.katana.features.explore.ui.viewmodel.ExploreViewModel
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface ExploreUiTestGraph : TestAppGraph {
    val exploreViewModel: ExploreViewModel

    @Provides fun exploreViewModel(): ExploreViewModel = ExploreViewModel()

    @DependencyGraph.Factory
    interface Factory {
        fun create(): ExploreUiTestGraph
    }
}

internal fun createExploreUiTestGraph() = createGraphFactory<ExploreUiTestGraph.Factory>().create()
