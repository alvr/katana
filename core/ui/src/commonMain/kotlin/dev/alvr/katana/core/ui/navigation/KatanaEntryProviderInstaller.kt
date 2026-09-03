package dev.alvr.katana.core.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import dev.alvr.katana.core.ui.navigation.destinations.KatanaDestination

typealias KatanaEntryProviderScope = EntryProviderScope<KatanaDestination>

typealias KatanaEntryProviderInstaller = KatanaEntryProviderScope.() -> Unit
