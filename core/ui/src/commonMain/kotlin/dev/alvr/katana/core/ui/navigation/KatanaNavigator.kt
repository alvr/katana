package dev.alvr.katana.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import dev.alvr.katana.core.ui.navigation.destinations.KatanaDestination
import dev.alvr.katana.core.ui.navigation.destinations.TopLevelDestination
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.SingleIn
import kotlinx.collections.immutable.toImmutableSet

@Stable
@AssistedInject
@SingleIn(AppScope::class)
class KatanaNavigator
internal constructor(
    @Assisted private val navState: KatanaNavState,
    private val entryProviders: Set<KatanaEntryProviderInstaller>,
) {
    val entries
        @Composable get() = navState.toDecoratedEntries(entryProviders.toImmutableSet())

    fun goBack() {
        val currentBackstack = navState.currentBackStack

        if (navState.bottomBarDestination == null) {
            if (currentBackstack.size > 1) {
                currentBackstack.removeLastOrNull()
            }
            return
        }
        if (currentBackstack.size == 1 && navState.bottomBarDestination != navState.primaryTopLevelDestination) {
            navState.bottomBarDestination = navState.primaryTopLevelDestination
        } else if (currentBackstack.size > 1) {
            currentBackstack.removeLastOrNull()
        }
    }

    fun add(route: KatanaDestination) {
        if (route is TopLevelDestination) {
            activate(route)
        } else {
            navState.currentBackStack.add(route)
        }
    }

    fun set(route: KatanaDestination) {
        navState.currentBackStack.clear()
        add(route)
    }

    fun activate(route: TopLevelDestination, withReselection: Boolean = true) {
        if (withReselection && route == navState.bottomBarDestination) {
            val currentBackstack = navState.currentBackStack

            if (currentBackstack.size > 1) {
                currentBackstack.removeRange(1, currentBackstack.size)
            }
            return
        }
        navState.bottomBarDestination = route
    }
}

val LocalNavigator = staticCompositionLocalOf<KatanaNavigator> { error("No KatanaNavigator provided") }
