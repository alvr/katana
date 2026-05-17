package dev.alvr.katana.core.common.di

import dev.alvr.katana.core.common.KatanaCachePath
import dev.alvr.katana.core.common.KatanaFilesPath
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import okio.Path.Companion.toOkioPath

@BindingContainer
@ContributesTo(AppScope::class)
actual object KatanaPathContainer {

    @Provides
    @SingleIn(AppScope::class)
    actual fun katanaCachePath(@AppContext context: PlatformContext): KatanaCachePath =
        KatanaCachePath(context.cacheDir.toOkioPath())

    @Provides
    @SingleIn(AppScope::class)
    actual fun katanaFilesPath(@AppContext context: PlatformContext): KatanaFilesPath =
        KatanaFilesPath(context.filesDir.toOkioPath())
}
