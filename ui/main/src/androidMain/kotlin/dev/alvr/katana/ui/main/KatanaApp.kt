package dev.alvr.katana.ui.main

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import dev.alvr.katana.ui.base.design.Katana
import dev.alvr.katana.ui.main.navigation.KatanaDestinations
import org.koin.androidx.compose.koinViewModel

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun KatanaApp() {
    val windowSizeClass = calculateWindowSizeClass()
    val useNavRail = windowSizeClass.widthSizeClass > WindowWidthSizeClass.Medium

    Katana {
        KatanaDestinations(
            useNavRail = useNavRail,
            vm = koinViewModel(),
        )
    }
}
