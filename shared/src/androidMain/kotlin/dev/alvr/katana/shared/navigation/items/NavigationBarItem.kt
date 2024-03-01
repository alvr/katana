package dev.alvr.katana.shared.navigation.items

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.NavGraphSpec

internal sealed interface NavigationBarItem {
    val direction: NavGraphSpec
    val icon: ImageVector
    @get:StringRes val label: Int
}
