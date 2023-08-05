package dev.alvr.katana.ui.login.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import dev.alvr.katana.ui.base.utils.Locales
import dev.alvr.katana.ui.base.utils.rememberKatanaStrings
import kotlinx.collections.immutable.persistentMapOf

@Immutable
@Suppress("LongParameterList", "UseDataClass")
class LoginStrings internal constructor(
    internal val headerKatanaDescription: String,

    internal val getStartedDescription: String,
    internal val getStartedButton: String,

    internal val beginDescription: String,
    internal val beginRegisterButton: String,
    internal val beginLoginButton: String,

    internal val fetchUserIdError: String,
    internal val saveTokenError: String,

    internal val contentDescriptionBackground: String,
    internal val contentDescriptionKatanaLogo: String,
    internal val contentDescriptionGetStartedArrow: String,
)

internal val Strings = persistentMapOf(
    Locales.EN to enLoginStrings,
    Locales.ES to esLoginStrings,
)

val LocalLoginStrings = compositionLocalOf { enLoginStrings }

@Composable
fun rememberLoginStrings() = rememberKatanaStrings(Strings)
