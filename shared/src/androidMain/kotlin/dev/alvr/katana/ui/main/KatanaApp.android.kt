package dev.alvr.katana.ui.main

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import dev.alvr.katana.ui.main.navigation.KatanaDestinations
import org.koin.androidx.compose.koinViewModel

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
internal actual fun KatanaContent() {
    KatanaDestinations(
        useNavRail = calculateWindowSizeClass().widthSizeClass > WindowWidthSizeClass.Medium,
        vm = koinViewModel(),
    )
}
