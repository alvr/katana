package dev.alvr.katana.core.common.di

import dev.alvr.katana.core.common.KatanaCachePath
import dev.alvr.katana.core.common.KatanaFilesPath
import okio.Path
import okio.Path.Companion.toPath
import org.koin.dsl.module

internal actual fun katanaPathModule() = module {
    single<KatanaCachePath> { KatanaCachePath(createDirectoryPath("cache")) }
    single<KatanaFilesPath> { KatanaFilesPath(createDirectoryPath("files")) }
}

private fun createDirectoryPath(directory: String): Path {
    val osName = System.getProperty("os.name").lowercase()
    val userHome = System.getProperty("user.home")

    val parent = when {
        osName.contains("win") -> {
            val appData = System.getenv("APPDATA")
            appData ?: "$userHome/AppData/Roaming"
        }

        osName.contains("mac") -> "$userHome/Library/Application Support"
        osName.contains("nix") ||
            osName.contains("nux") ||
            osName.contains("aix") -> "$userHome/.config"

        else -> throw UnsupportedOperationException("Unsupported OS: $osName")
    } + KatanaDir

    return parent.toPath().resolve(directory)
}

private const val KatanaDir = "/alvr.dev/Katana"
