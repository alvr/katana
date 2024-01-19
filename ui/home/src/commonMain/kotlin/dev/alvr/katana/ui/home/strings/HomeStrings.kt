package dev.alvr.katana.ui.home.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import dev.alvr.katana.ui.base.utils.Locales
import dev.alvr.katana.ui.base.utils.rememberKatanaStrings
import kotlinx.collections.immutable.persistentMapOf

@Immutable
@Suppress("LongParameterList", "UseDataClass")
class HomeStrings internal constructor(
    internal val animeListNav: String = "Anime",
    internal val mangaListNav: String = "Manga",
    internal val exploreListNav: String,
    internal val socialListNav: String,
    internal val accountListNav: String,
)

internal val Strings = persistentMapOf(
    Locales.EN to enHomeStrings,
    Locales.ES to esHomeStrings,
)

val LocalHomeStrings = compositionLocalOf { enHomeStrings }

@Composable
fun rememberHomeStrings() = rememberKatanaStrings(Strings)
