package dev.alvr.katana.ui.base.design

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dev.alvr.katana.ui.base.R

private val Barlow = FontFamily(
    Font(R.font.barlow_light, FontWeight.Light),
    Font(R.font.barlow_normal, FontWeight.Normal),
    Font(R.font.barlow_medium, FontWeight.Medium),
    Font(R.font.barlow_bold, FontWeight.Bold),
)

internal val KatanaTypography = Typography(defaultFontFamily = Barlow)
