package dev.alvr.katana.core.ui.theme

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable

val WindowInsets.Companion.noInsets: WindowInsets
    get() = WindowInsets()

val WindowInsets.Companion.contentPaddingSmall: WindowInsets
    @Composable
    get() =
        WindowInsets(
            left = KatanaTheme.dimensions.contentPaddingSmall,
            top = KatanaTheme.dimensions.contentPaddingSmall,
            right = KatanaTheme.dimensions.contentPaddingSmall,
            bottom = KatanaTheme.dimensions.contentPaddingSmall,
        )

val WindowInsets.Companion.contentPaddingMedium: WindowInsets
    @Composable
    get() =
        WindowInsets(
            left = KatanaTheme.dimensions.contentPaddingMedium,
            top = KatanaTheme.dimensions.contentPaddingMedium,
            right = KatanaTheme.dimensions.contentPaddingMedium,
            bottom = KatanaTheme.dimensions.contentPaddingMedium,
        )

val WindowInsets.Companion.contentPaddingLarge: WindowInsets
    @Composable
    get() =
        WindowInsets(
            left = KatanaTheme.dimensions.contentPaddingLarge,
            top = KatanaTheme.dimensions.contentPaddingLarge,
            right = KatanaTheme.dimensions.contentPaddingLarge,
            bottom = KatanaTheme.dimensions.contentPaddingLarge,
        )
