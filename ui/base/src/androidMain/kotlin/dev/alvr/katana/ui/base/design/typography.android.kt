package dev.alvr.katana.ui.base.design

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import dev.alvr.katana.ui.base.resources.KatanaResource

private val cache = mutableMapOf<String, Font>()

@Composable
@SuppressLint("DiscouragedApi")
internal actual fun font(font: KatanaResource, weight: FontWeight, style: FontStyle) =
    with(LocalContext.current) {
        cache.getOrPut(font.id) {
            val id = resources.getIdentifier(font.id, "font", packageName)
            Font(id, weight, style)
        }
    }
