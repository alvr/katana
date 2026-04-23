package dev.alvr.katana.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import co.touchlab.kermit.DefaultFormatter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.platformLogWriter
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import dev.alvr.katana.core.common.KatanaBuildConfig
import dev.alvr.katana.core.ui.components.snackbar.SnackbarController
import dev.alvr.katana.core.ui.theme.KatanaTheme
import dev.alvr.katana.shared.screens.Katana
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory

@Inject
@Composable
fun Katana(viewModelFactory: MetroViewModelFactory, imageLoader: ImageLoader, snackbarController: SnackbarController) {
    Init(imageLoader)

    KatanaTheme {
        CompositionLocalProvider(LocalMetroViewModelFactory provides viewModelFactory) { Katana(snackbarController) }
    }
}

@Composable
private fun Init(imageLoader: ImageLoader) {
    setSingletonImageLoaderFactory { imageLoader }

    LaunchedEffect(Unit) {
        if (KatanaBuildConfig.DEBUG) {
            Logger.setLogWriters(platformLogWriter(DefaultFormatter))
        }
    }
}
