package dev.alvr.katana.core.ui.navigation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import dev.alvr.katana.core.ui.navigation.destinations.HomeDestination
import dev.alvr.katana.core.ui.navigation.destinations.KatanaDestination
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@Stable
sealed interface KatanaNavigator {
    val backStack: SnapshotStateList<KatanaDestination>
    val entryProvider: (KatanaDestination) -> NavEntry<KatanaDestination>

    fun goBack()
}

@Stable
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class KatanaNavigatorImpl(private val entryProviders: Set<KatanaEntryProviderInstaller>) : KatanaNavigator {
    override val backStack = mutableStateListOf<KatanaDestination>(HomeDestination.Root)
    override val entryProvider = entryProvider { entryProviders.forEach { provider -> provider() } }

    override fun goBack() {
        backStack.removeLastOrNull()
    }
}
