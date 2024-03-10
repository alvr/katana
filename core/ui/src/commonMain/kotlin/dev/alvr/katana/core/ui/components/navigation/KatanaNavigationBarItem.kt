package dev.alvr.katana.core.ui.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

interface KatanaNavigationBarItem {
    val key: Any
    val selectedIcon: ImageVector
    val unselectedIcon: ImageVector
    @get:Composable val label: String
}
