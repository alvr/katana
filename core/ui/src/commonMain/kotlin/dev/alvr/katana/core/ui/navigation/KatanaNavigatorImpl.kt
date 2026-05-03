package dev.alvr.katana.core.ui.navigation

import androidx.compose.runtime.SnapshotFlowManager
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.entryProvider
import dev.alvr.katana.core.ui.navigation.destinations.KatanaDestination
import dev.alvr.katana.core.ui.navigation.destinations.MainDestination
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@Stable
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class KatanaNavigatorImpl(private val entryProviders: Set<KatanaEntryProviderInstaller>) : KatanaNavigator {
    override val backStack = mutableStateListOf<KatanaDestination>(MainDestination.Home())
    override val entryProvider = entryProvider { entryProviders.forEach { provider -> provider() } }

    override fun goBack() {
        backStack.removeLastOrNull()
    }
}
