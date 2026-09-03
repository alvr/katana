package dev.alvr.katana.core.ui.navigation.destinations

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
sealed interface TopLevelDestination : KatanaDestination {

    @Serializable data object Home : TopLevelDestination

    @Serializable data object Anime : TopLevelDestination

    @Serializable data object Manga : TopLevelDestination

    @Serializable data object Explore : TopLevelDestination

    @Serializable data object Account : TopLevelDestination
}
