package dev.alvr.katana.core.common.di

import dev.alvr.katana.core.common.KatanaCachePath
import dev.alvr.katana.core.common.KatanaFilesPath
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn

expect object KatanaPathContainer {

    @SingleIn(AppScope::class) fun katanaCachePath(@AppContext context: PlatformContext): KatanaCachePath

    @SingleIn(AppScope::class) fun katanaFilesPath(@AppContext context: PlatformContext): KatanaFilesPath
}
