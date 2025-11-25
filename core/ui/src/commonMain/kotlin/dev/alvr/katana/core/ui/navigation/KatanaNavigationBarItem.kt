package dev.alvr.katana.core.ui.navigation

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import dev.alvr.katana.core.ui.utils.hasParentRoute
import dev.alvr.katana.core.ui.utils.hasRoute
import org.jetbrains.compose.resources.StringResource

@Stable
interface KatanaNavigationBarItem {
    val screen: KatanaDestination
    val icon: ImageVector
    val label: StringResource

    companion object {
        fun <T : KatanaNavigationBarItem> NavBackStackEntry?.hasRoute(screen: T) =
            hasRoute(screen.screen::class) || hasParentRoute(screen.screen::class)
    }
}
