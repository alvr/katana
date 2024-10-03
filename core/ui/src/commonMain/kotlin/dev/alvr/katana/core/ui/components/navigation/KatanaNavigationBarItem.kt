package dev.alvr.katana.core.ui.components.navigation

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.alvr.katana.core.ui.destinations.KatanaDestination
import org.jetbrains.compose.resources.StringResource

@Stable
interface KatanaNavigationBarItem {
    val screen: KatanaDestination
    val selectedIcon: ImageVector
    val unselectedIcon: ImageVector
    val label: StringResource
}
