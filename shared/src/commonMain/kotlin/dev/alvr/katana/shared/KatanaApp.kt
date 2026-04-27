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
import com.skydoves.compose.stability.runtime.ComposeStabilityAnalyzer
import dev.alvr.katana.core.common.KatanaBuildConfig
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
    imageLoader: ImageLoader,
    snackbarController: SnackbarController,
) {
    Init(imageLoader)
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
private fun Init(imageLoader: ImageLoader) {
    setSingletonImageLoaderFactory { imageLoader }

    DisposableEffect(Unit) {
        ComposeStabilityAnalyzer.setEnabled(KatanaBuildConfig.ENABLE_TRACE_RECOMPOSITION)

        if (KatanaBuildConfig.DEBUG) {
            Logger.setLogWriters(platformLogWriter(DefaultFormatter))
        }

        onDispose {}
    }
}
