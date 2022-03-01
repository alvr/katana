package dev.alvr.katana.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

@OptIn(ExperimentalAnimationApi::class)
private typealias AnimatedEnter = AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?

@OptIn(ExperimentalAnimationApi::class)
private typealias AnimatedExit = AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?

private const val MEDIUM_DURATION = 500

@ExperimentalAnimationApi
internal val homeEnterTransition: AnimatedEnter = {
    fadeIn(animationSpec = tween(MEDIUM_DURATION))
}

@ExperimentalAnimationApi
internal val homeExitTransition: AnimatedExit = {
    fadeOut(animationSpec = tween(MEDIUM_DURATION))
}
