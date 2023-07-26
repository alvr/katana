package dev.alvr.katana.ui.base.design

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
@SuppressLint("DiscouragedApi")
internal actual fun font(font: String, weight: FontWeight, style: FontStyle): Font {
    val id = with(LocalContext.current) { resources.getIdentifier(font, "font", packageName) }
    return Font(id, weight, style)
}
