package dev.alvr.katana.features.explore.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import dev.alvr.katana.core.ui.utils.Locales
import dev.alvr.katana.core.ui.utils.rememberKatanaStrings
import kotlinx.collections.immutable.persistentMapOf

@Immutable
@Suppress("LongParameterList", "UseDataClass")
class ExploreStrings internal constructor(
    internal val exploreToolbarTitle: String,
    internal val exploreToolbarSearchPlaceholder: String,
)

internal val Strings = persistentMapOf(
    Locales.EN to enExploreStrings,
    Locales.ES to esExploreStrings,
)

val LocalExploreStrings = compositionLocalOf { enExploreStrings }

@Composable
fun rememberExploreStrings() = rememberKatanaStrings(Strings)
