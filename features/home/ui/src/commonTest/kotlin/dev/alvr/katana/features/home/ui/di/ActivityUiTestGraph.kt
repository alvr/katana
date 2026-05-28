package dev.alvr.katana.features.home.ui.di

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

    /**
 * Provides a default ActivityViewModel instance for the test dependency graph.
 *
 * @return A new ActivityViewModel instance.
 */
@Provides fun activityViewModel(): ActivityViewModel = ActivityViewModel()

    @DependencyGraph.Factory
    interface Factory {
        /**
 * Creates the test dependency graph used by the Activity UI.
 *
 * @return An ActivityUiTestGraph instance configured for the test scope.
 */
fun create(): ActivityUiTestGraph
    }
}

/**
 * Creates the test dependency graph that provides dependencies for the ActivityViewModel used in UI tests.
 *
 * @return The created ActivityUiTestGraph instance.
 */
internal fun createActivityUiTestGraph() = createGraphFactory<ActivityUiTestGraph.Factory>().create()
