package dev.alvr.katana.ui.base.design

import androidx.compose.material.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import dev.alvr.katana.ui.base.R

@ExperimentalTextApi
private val Barlow = getGoogleFontFamily(
    name = "Barlow",
    weights = listOf(
        FontWeight.Light,
        FontWeight.Normal,
        FontWeight.Medium,
        FontWeight.Bold,
    ),
)

@ExperimentalTextApi
private fun getGoogleFontFamily(
    name: String,
    weights: List<FontWeight>,
    provider: GoogleFont.Provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs,
    ),
) = FontFamily(weights.map { Font(GoogleFont(name), provider, it) })

@OptIn(ExperimentalTextApi::class)
internal val KatanaTypography = Typography(defaultFontFamily = Barlow)
