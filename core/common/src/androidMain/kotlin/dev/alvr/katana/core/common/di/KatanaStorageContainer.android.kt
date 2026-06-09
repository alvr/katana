package dev.alvr.katana.core.common.di

import dev.alvr.katana.core.common.KatanaStorage
import dev.alvr.katana.core.common.KatanaStorageImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import okio.Path.Companion.toOkioPath

@BindingContainer
@ContributesTo(AppScope::class)
actual object KatanaStorageContainer {

    @Provides
    @SingleIn(AppScope::class)
    actual fun katanaStorage(@AppContext context: PlatformContext): KatanaStorage =
        KatanaStorageImpl(files = context.filesDir.toOkioPath(), cache = context.cacheDir.toOkioPath())
}
