package dev.alvr.katana.shared.di

import dev.alvr.katana.core.common.di.AppContext
import dev.alvr.katana.core.common.di.PlatformContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@DependencyGraph(AppScope::class, bindingContainers = [KatanaEntryProviderInstallerContainer::class])
internal interface AppGraph : KatanaGraph {

    @Provides @AppContext @SingleIn(AppScope::class) fun appContext(): PlatformContext = PlatformContext
}
