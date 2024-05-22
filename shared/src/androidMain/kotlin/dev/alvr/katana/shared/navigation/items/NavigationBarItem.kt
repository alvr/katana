package dev.alvr.katana.shared.navigation.items

import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.NavGraphSpec
import org.jetbrains.compose.resources.StringResource

internal sealed interface NavigationBarItem {
    val direction: NavGraphSpec
    val icon: ImageVector
    val label: StringResource
}
