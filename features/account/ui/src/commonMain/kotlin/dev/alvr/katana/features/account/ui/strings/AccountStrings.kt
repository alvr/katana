package dev.alvr.katana.features.account.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import dev.alvr.katana.core.ui.utils.Locales
import dev.alvr.katana.core.ui.utils.rememberKatanaStrings
import kotlinx.collections.immutable.persistentMapOf

@Immutable
@Suppress("LongParameterList", "UseDataClass")
class AccountStrings internal constructor(
    internal val title: String,
    internal val logoutButton: String,
)

internal val Strings = persistentMapOf(
    Locales.EN to enAccountStrings,
    Locales.ES to esAccountStrings,
)

val LocalAccountStrings = compositionLocalOf { enAccountStrings }

@Composable
fun rememberAccountStrings() = rememberKatanaStrings(Strings)
