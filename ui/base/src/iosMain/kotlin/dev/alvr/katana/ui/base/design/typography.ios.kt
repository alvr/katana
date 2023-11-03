package dev.alvr.katana.ui.base.design

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import dev.alvr.katana.ui.base.resources.KatanaResource

private val cache = mutableMapOf<String, Font>()

@Composable
internal actual fun font(font: KatanaResource, weight: FontWeight, style: FontStyle) =
    cache.getOrPut(font.id) {
        Font(font.id, font.asByteArray, weight, style)
    }
