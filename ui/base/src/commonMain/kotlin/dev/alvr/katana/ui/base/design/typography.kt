package dev.alvr.katana.ui.base.design

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import dev.alvr.katana.ui.base.resources.KatanaResource
import dev.alvr.katana.ui.base.resources.KatanaResources

internal val KatanaTypography
    @Composable get() = Typography(
        defaultFontFamily = FontFamily(
            font(KatanaResources.barlowLight, FontWeight.Light, FontStyle.Normal),
            font(KatanaResources.barlowNormal, FontWeight.Normal, FontStyle.Normal),
            font(KatanaResources.barlowMedium, FontWeight.Medium, FontStyle.Normal),
            font(KatanaResources.barlowBold, FontWeight.Bold, FontStyle.Normal),
        ),
    )

@Composable
internal expect fun font(font: KatanaResource, weight: FontWeight, style: FontStyle): Font
