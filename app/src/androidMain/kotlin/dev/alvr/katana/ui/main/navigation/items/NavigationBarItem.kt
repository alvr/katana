package dev.alvr.katana.ui.main.navigation.items

import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.NavGraphSpec
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

@OptIn(ExperimentalResourceApi::class)
internal sealed interface NavigationBarItem {
    val direction: NavGraphSpec
    val icon: ImageVector
    val label: StringResource
}
