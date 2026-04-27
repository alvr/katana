package dev.alvr.katana.shared

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import co.touchlab.kermit.DefaultFormatter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.platformLogWriter
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.crossfade
import com.skydoves.compose.stability.runtime.ComposeStabilityAnalyzer
import dev.alvr.katana.core.common.KatanaBuildConfig
import dev.alvr.katana.core.common.KatanaStorage
import dev.alvr.katana.core.ui.components.snackbar.LocalSnackbarController
import dev.alvr.katana.core.ui.components.snackbar.SnackbarController
import dev.alvr.katana.core.ui.navigation.KatanaNavigator
import dev.alvr.katana.core.ui.theme.KatanaTheme
import dev.alvr.katana.shared.screens.Katana
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory

@Inject
@Composable
fun Katana(
    viewModelFactory: MetroViewModelFactory,
    navigator: () -> KatanaNavigator,
    storage: KatanaStorage,
    snackbarController: SnackbarController,
) {
    Init(storage)
    val navigator = remember { navigator() }

    SharedTransitionLayout {
        KatanaTheme {
            CompositionLocalProvider(
                LocalMetroViewModelFactory provides viewModelFactory,
                LocalSnackbarController provides snackbarController,
            ) {
                Katana(navigator)
            }
        }
    }
}

@Composable
private fun Init(storage: KatanaStorage) {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .memoryCache { MemoryCache.Builder().maxSizePercent(context, CoilMemoryCachePercent).build() }
            .diskCache {
                DiskCache.Builder()
                    .directory(storage.cache / CoilImagesPath)
                    .maxSizePercent(CoilDiskCachePercent)
                    .build()
            }
            .build()
    }

    DisposableEffect(Unit) {
        ComposeStabilityAnalyzer.setEnabled(KatanaBuildConfig.ENABLE_TRACE_RECOMPOSITION)

        if (KatanaBuildConfig.DEBUG) {
            Logger.setLogWriters(platformLogWriter(DefaultFormatter))
        }

        onDispose {}
    }
}

private const val CoilDiskCachePercent = 0.25
private const val CoilMemoryCachePercent = 0.05
private const val CoilImagesPath = "images_cache"
