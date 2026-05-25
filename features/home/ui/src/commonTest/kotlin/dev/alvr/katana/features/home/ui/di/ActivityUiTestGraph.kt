package dev.alvr.katana.features.home.ui.di

import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.tests.di.TestAppGraph
import dev.alvr.katana.core.tests.di.TestAppScope
import dev.alvr.katana.features.home.ui.screens.activity.viewmodel.ActivityViewModel
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory

@SingleIn(TestAppScope::class)
@DependencyGraph(TestAppScope::class)
internal interface ActivityUiTestGraph : TestAppGraph {
    val activityViewModel: ActivityViewModel

    @Provides fun activityViewModel(dispatcher: KatanaDispatcher): ActivityViewModel = ActivityViewModel(dispatcher)

    @DependencyGraph.Factory
    interface Factory {
        fun create(): ActivityUiTestGraph
    }
}

internal fun createActivityUiTestGraph() = createGraphFactory<ActivityUiTestGraph.Factory>().create().activityViewModel
