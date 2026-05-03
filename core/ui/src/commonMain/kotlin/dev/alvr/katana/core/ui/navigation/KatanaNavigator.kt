package dev.alvr.katana.core.ui.navigation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation3.runtime.NavEntry
import dev.alvr.katana.core.ui.navigation.destinations.KatanaDestination

@Stable
sealed interface KatanaNavigator {
    val backStack: SnapshotStateList<KatanaDestination>
    val entryProvider: (KatanaDestination) -> NavEntry<KatanaDestination>

    fun goBack()
}

val LocalNavigator = staticCompositionLocalOf<KatanaNavigator> { error("No KatanaNavigator provided") }
