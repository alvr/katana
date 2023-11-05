package dev.alvr.katana.ui.base.design

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

private val cache = mutableMapOf<String, Font>()

@Composable
@SuppressLint("DiscouragedApi")
internal actual fun font(font: String, weight: FontWeight, style: FontStyle) =
    with(LocalContext.current) {
        cache.getOrPut(font) {
            val id = resources.getIdentifier(font, "font", packageName)
            Font(id, weight, style)
        }
    }
