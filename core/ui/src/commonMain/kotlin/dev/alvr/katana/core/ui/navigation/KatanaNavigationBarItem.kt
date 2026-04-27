package dev.alvr.katana.core.ui.navigation

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.alvr.katana.core.ui.navigation.destinations.KatanaDestination
import org.jetbrains.compose.resources.StringResource

@Stable
interface KatanaNavigationBarItem {
    val screen: KatanaDestination
    val icon: ImageVector
    val label: StringResource
}
