package dev.alvr.katana.core.common.di

import android.content.Context
import dev.alvr.katana.core.common.KatanaCachePath
import dev.alvr.katana.core.common.KatanaFilesPath
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import okio.Path.Companion.toOkioPath

@ContributesTo(AppScope::class)
interface KatanaPathProviders {

    @Provides
    @SingleIn(AppScope::class)
    fun katanaCachePath(@AppContext context: Context): KatanaCachePath = KatanaCachePath(context.cacheDir.toOkioPath())

    @Provides
    @SingleIn(AppScope::class)
    fun katanaFilesPath(@AppContext context: Context): KatanaFilesPath = KatanaFilesPath(context.filesDir.toOkioPath())
}
