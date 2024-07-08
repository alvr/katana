package dev.alvr.katana.core.ui.components.navigation

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.alvr.katana.core.ui.screens.KatanaScreen
import org.jetbrains.compose.resources.StringResource

@Stable
interface KatanaNavigationBarItem {
    val screen: KatanaScreen
    val selectedIcon: ImageVector
    val unselectedIcon: ImageVector
    val label: StringResource
}
