package dev.alvr.katana.core.common.di

import dev.alvr.katana.core.common.KatanaCachePath
import dev.alvr.katana.core.common.KatanaFilesPath
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@BindingContainer
@ContributesTo(AppScope::class)
actual object KatanaPathContainer {

    @Provides
    @SingleIn(AppScope::class)
    actual fun katanaCachePath(@AppContext context: PlatformContext): KatanaCachePath =
        KatanaCachePath(createDirectoryPath(NSCachesDirectory))

    @Provides
    @SingleIn(AppScope::class)
    actual fun katanaFilesPath(@AppContext context: PlatformContext): KatanaFilesPath =
        KatanaFilesPath(createDirectoryPath(NSDocumentDirectory))
}

@OptIn(ExperimentalForeignApi::class)
private fun createDirectoryPath(directory: ULong): Path {
    val url =
        NSFileManager.defaultManager.URLForDirectory(
            directory = directory,
            appropriateForURL = null,
            create = true,
            inDomain = NSUserDomainMask,
            error = null,
        )

    return with(url?.path) {
        requireNotNull(this) { "Failed to get path from URL: $url" }
        toPath()
    }
}
