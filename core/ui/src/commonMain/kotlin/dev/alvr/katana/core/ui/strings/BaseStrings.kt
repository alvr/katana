package dev.alvr.katana.core.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import dev.alvr.katana.core.ui.utils.Locales
import dev.alvr.katana.core.ui.utils.rememberKatanaStrings
import kotlinx.collections.immutable.persistentMapOf

@Immutable
@Suppress("LongParameterList", "UseDataClass")
class BaseStrings internal constructor(
    internal val componentEmptyState: String,
    internal val componentErrorState: String,
    internal val componentErrorStateRetryButton: String,
    internal val toolbarMenuSearch: String,
    internal val toolbarMenuFilter: String,
    internal val toolbarSearchClose: String,
    internal val toolbarSearchClear: String,
)

internal val Strings = persistentMapOf(
    Locales.EN to enBaseStrings,
    Locales.ES to esBaseStrings,
)

val LocalBaseStrings = compositionLocalOf { enBaseStrings }

@Composable
fun rememberBaseStrings() = rememberKatanaStrings(Strings)
