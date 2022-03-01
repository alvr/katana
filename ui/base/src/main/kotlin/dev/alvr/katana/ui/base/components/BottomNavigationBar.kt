package dev.alvr.katana.ui.base.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.alvr.katana.ui.base.navigation.NavigationBarDestination

@Composable
fun BottomNavigationBar(
    isVisible: Boolean,
    currentRoute: String?,
    destinations: Array<out NavigationBarDestination>,
    navigation: (String) -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            BottomNavigation {
                destinations.forEach { destination ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = stringResource(id = destination.label)
                            )
                        },
                        label = { Text(text = stringResource(id = destination.label)) },
                        alwaysShowLabel = true,
                        selected = destination.route == currentRoute,
                        onClick = { navigation(destination.route) }
                    )
                }
            }
        }
    )
}
