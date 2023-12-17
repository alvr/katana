package dev.alvr.katana.ui.main

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import dev.alvr.katana.ui.base.design.KatanaTheme
import dev.alvr.katana.ui.main.di.katanaModule
import dev.alvr.katana.ui.main.di.platformModule
import dev.alvr.katana.ui.main.navigation.KatanaDestinations
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
internal fun KatanaApp() {
    initApp()

    KatanaTheme {
        KatanaDI {
            KatanaStrings {
                KatanaDestinations(
                    useNavRail = calculateWindowSizeClass().widthSizeClass > WindowWidthSizeClass.Medium,
                    vm = koinViewModel(),
                )
            }
        }
    }
}

@Composable
private fun KatanaDI(
    content: @Composable () -> Unit,
) {
    val platformModule = platformModule()

    KoinApplication(
        application = { modules(katanaModule, platformModule) },
        content = content,
    )
}
