package dev.alvr.katana.core.ui.components.navigation

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.resources.value
import kotlinx.collections.immutable.ImmutableList

fun <T : KatanaNavigationBarItem> NavigationSuiteScope.katanaNavigationBar(
    items: ImmutableList<T>,
    selected: (T) -> Boolean,
    onClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = false,
    badge: (@Composable () -> Unit)? = null,
    colors: NavigationSuiteItemColors? = null,
    interactionSource: MutableInteractionSource? = null
) {
    items.forEach { item ->
        val isSelected = selected(item)

        item(
            modifier = modifier,
            selected = isSelected,
            onClick = { onClick(item) },
            icon = { NavigationBarIcon(item, isSelected) },
            label = { NavigationBarLabel(item) },
            enabled = enabled,
            alwaysShowLabel = alwaysShowLabel,
            badge = badge,
            colors = colors,
            interactionSource = interactionSource,
        )
    }
}

@Composable
private fun NavigationBarIcon(
    destination: KatanaNavigationBarItem,
    selected: Boolean,
) {
    Icon(
        imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
        contentDescription = destination.label.value,
    )
}

@Composable
private fun NavigationBarLabel(destination: KatanaNavigationBarItem) {
    Text(text = destination.label.value)
}
