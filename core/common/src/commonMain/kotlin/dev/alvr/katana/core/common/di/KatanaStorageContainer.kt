package dev.alvr.katana.core.common.di

import dev.alvr.katana.core.common.KatanaStorage
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.SingleIn

expect object KatanaStorageContainer {

    @SingleIn(AppScope::class) fun katanaStorage(@AppContext context: PlatformContext): KatanaStorage
}
