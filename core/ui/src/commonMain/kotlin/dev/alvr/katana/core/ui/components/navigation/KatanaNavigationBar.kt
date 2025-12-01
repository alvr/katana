package dev.alvr.katana.core.ui.components.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach
import dev.alvr.katana.core.ui.navigation.KatanaNavigationBarItem
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.utils.isLandscape
import kotlinx.collections.immutable.ImmutableList

@Composable
fun <T : KatanaNavigationBarItem> KatanaNavigationBar(
    items: ImmutableList<T>,
    type: KatanaNavigationBarType,
    isSelected: (T) -> Boolean,
    onClick: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isLandscape = isLandscape()

    when {
        type == KatanaNavigationBarType.Bottom && !isLandscape -> BottomNavigationBar(
            items = items,
            isSelected = isSelected,
            onClick = onClick,
            modifier = modifier,
        )

        type == KatanaNavigationBarType.Rail && isLandscape -> RailNavigationBar(
            items = items,
            isSelected = isSelected,
            onClick = onClick,
            modifier = modifier,
        )
    }
}

@Composable
private fun <T : KatanaNavigationBarItem> BottomNavigationBar(
    items: ImmutableList<T>,
    isSelected: (T) -> Boolean,
    onClick: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier) {
        items.fastForEach { item ->
            val selected = isSelected(item)

            NavigationBarItem(
                icon = { NavigationBarIcon(item) },
                label = { NavigationBarLabel(item) },
                selected = selected,
                onClick = { onClick(item) },
                alwaysShowLabel = false,
            )
        }
    }
}

@Composable
private fun <T : KatanaNavigationBarItem> RailNavigationBar(
    items: ImmutableList<T>,
    isSelected: (T) -> Boolean,
    onClick: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRail(modifier = modifier) {
        Spacer(Modifier.weight(1f))
        items.fastForEach { item ->
            val selected = isSelected(item)

            NavigationRailItem(
                icon = { NavigationBarIcon(item) },
                label = { NavigationBarLabel(item) },
                selected = selected,
                onClick = { onClick(item) },
                alwaysShowLabel = false,
            )
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun NavigationBarIcon(
    destination: KatanaNavigationBarItem,
) {
    Icon(
        imageVector = destination.icon,
        contentDescription = destination.label.value,
    )
}

@Composable
private fun NavigationBarLabel(destination: KatanaNavigationBarItem) {
    Text(text = destination.label.value)
}

enum class KatanaNavigationBarType {
    Bottom,
    Rail,
}
