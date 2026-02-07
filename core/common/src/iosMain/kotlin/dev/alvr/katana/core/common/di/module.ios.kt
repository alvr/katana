package dev.alvr.katana.core.common.di

import dev.alvr.katana.core.common.KatanaCachePath
import dev.alvr.katana.core.common.KatanaFilesPath
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path
import okio.Path.Companion.toPath
import org.koin.dsl.module
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

internal actual fun katanaPathModule() = module {
    single<KatanaCachePath> { KatanaCachePath(createDirectoryPath(NSCachesDirectory)) }
    single<KatanaFilesPath> { KatanaFilesPath(createDirectoryPath(NSDocumentDirectory)) }
}

@OptIn(ExperimentalForeignApi::class)
private fun createDirectoryPath(directory: ULong): Path =
    NSFileManager.defaultManager
        .URLForDirectory(
            directory = directory,
            appropriateForURL = null,
            create = false,
            inDomain = NSUserDomainMask,
            error = null,
        )!!
        .path!!
        .toPath()
