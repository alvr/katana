package dev.alvr.katana.ui.base.design

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

internal val KatanaTypography
    @Composable get() = Typography(
        defaultFontFamily = FontFamily(
            font("barlow_light", FontWeight.Light, FontStyle.Normal),
            font("barlow_normal", FontWeight.Normal, FontStyle.Normal),
            font("barlow_medium", FontWeight.Medium, FontStyle.Normal),
            font("barlow_bold", FontWeight.Bold, FontStyle.Normal),
        ),
    )

@Composable
internal expect fun font(font: String, weight: FontWeight, style: FontStyle): Font
