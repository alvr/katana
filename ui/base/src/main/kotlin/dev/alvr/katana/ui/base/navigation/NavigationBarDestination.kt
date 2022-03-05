package dev.alvr.katana.ui.base.navigation

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector

@Stable
@Immutable
interface NavigationBarDestination {
    @get:StringRes
    val label: Int
    val icon: ImageVector
    val route: String
}
