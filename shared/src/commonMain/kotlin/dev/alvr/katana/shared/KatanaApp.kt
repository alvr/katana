package dev.alvr.katana.shared

import androidx.compose.runtime.Composable
import co.touchlab.kermit.DefaultFormatter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.platformLogWriter
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.crossfade
import dev.alvr.katana.core.common.KatanaBuildConfig
import dev.alvr.katana.core.common.KatanaCachePath
import dev.alvr.katana.core.ui.theme.KatanaTheme
import dev.alvr.katana.shared.di.katanaModule
import dev.alvr.katana.shared.screens.Katana
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.logger.Level
import org.koin.dsl.KoinConfiguration

@Composable
@OptIn(KoinExperimentalAPI::class)
fun Katana() {
    initNapier()

    KoinApplication(
        configuration = KoinConfiguration { modules(katanaModule) },
        logLevel = if (KatanaBuildConfig.DEBUG) Level.DEBUG else Level.NONE,
    ) {
        InitCoil()
        KatanaTheme { Katana() }
    }
}

@Composable
private fun InitCoil() {
    val diskCacheDir = koinInject<KatanaCachePath>().resolve(CoilImagesPath).path

    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .memoryCache { MemoryCache.Builder().maxSizePercent(context, CoilMemoryCachePercent).build() }
            .diskCache { DiskCache.Builder().directory(diskCacheDir).maxSizePercent(CoilDiskCachePercent).build() }
            .build()
    }
}

private fun initNapier() {
    if (KatanaBuildConfig.DEBUG) {
        Logger.setLogWriters(platformLogWriter(DefaultFormatter))
    }
}

private const val CoilDiskCachePercent = 0.25
private const val CoilMemoryCachePercent = 0.05
private const val CoilImagesPath = "images_cache"
