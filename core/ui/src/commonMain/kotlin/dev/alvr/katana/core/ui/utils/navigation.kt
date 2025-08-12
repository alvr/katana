package dev.alvr.katana.core.ui.utils

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavDestination.Companion.hasRoute
import dev.alvr.katana.core.ui.navigation.KatanaDestination
import kotlin.reflect.KClass

fun navDeepLink(deepLinkBuilder: NavDeepLink.Builder.() -> Unit): NavDeepLink =
    NavDeepLink.Builder().apply(deepLinkBuilder).build()

fun <T : KatanaDestination> NavBackStackEntry?.hasRoute(route: KClass<T>) =
    this?.destination?.hasRoute(route) ?: false

fun <T : KatanaDestination> NavBackStackEntry?.hasParentRoute(route: KClass<T>) =
    this?.destination?.parent?.hasRoute(route) ?: false
