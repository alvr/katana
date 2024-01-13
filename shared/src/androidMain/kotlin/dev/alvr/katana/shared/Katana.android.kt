package dev.alvr.katana.shared

import androidx.compose.runtime.Composable
import dev.alvr.katana.core.ui.utils.isLandscape
import dev.alvr.katana.shared.navigation.KatanaDestinations
import org.koin.androidx.compose.koinViewModel

@Composable
internal actual fun KatanaContent() {
    KatanaDestinations(
        useNavRail = isLandscape(),
        vm = koinViewModel(),
    )
}
