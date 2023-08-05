package dev.alvr.katana.ui.base.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.lyricist.LanguageTag
import cafe.adriel.lyricist.Lyricist
import kotlinx.collections.immutable.ImmutableMap

@Composable
fun <T> rememberKatanaStrings(strings: ImmutableMap<LanguageTag, T>): T = remember {
    Lyricist(Locale.current.toLanguageTag(), strings).strings
}

object Locales {
    const val EN = "en"
    const val ES = "es"
}
