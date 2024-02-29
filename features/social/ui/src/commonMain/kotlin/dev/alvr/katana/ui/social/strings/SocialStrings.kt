package dev.alvr.katana.ui.social.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import dev.alvr.katana.ui.base.utils.Locales
import dev.alvr.katana.ui.base.utils.rememberKatanaStrings
import kotlinx.collections.immutable.persistentMapOf

@Immutable
@Suppress("LongParameterList", "UseDataClass")
class SocialStrings internal constructor(
    internal val socialToolbarTitle: String,
    internal val socialToolbarSearchPlaceholder: String,
)

internal val Strings = persistentMapOf(
    Locales.EN to enSocialStrings,
    Locales.ES to esSocialStrings,
)

val LocalSocialStrings = compositionLocalOf { enSocialStrings }

@Composable
fun rememberSocialStrings() = rememberKatanaStrings(Strings)
