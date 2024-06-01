package dev.alvr.katana.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import io.sentry.compose.withSentryObservableEffect

@Composable
internal actual fun NavHostController.sentryObserver() = withSentryObservableEffect()
