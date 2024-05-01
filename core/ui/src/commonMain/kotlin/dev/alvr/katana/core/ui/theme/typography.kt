package dev.alvr.katana.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import dev.alvr.katana.core.ui.resources.Res
import dev.alvr.katana.core.ui.resources.barlow_bold
import dev.alvr.katana.core.ui.resources.barlow_light
import dev.alvr.katana.core.ui.resources.barlow_medium
import dev.alvr.katana.core.ui.resources.barlow_normal
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

@OptIn(ExperimentalResourceApi::class)
internal val KatanaTypography
    @Composable get() = Typography().apply {
        val fontFamily = FontFamily(
            Font(Res.font.barlow_light, FontWeight.Light, FontStyle.Normal),
            Font(Res.font.barlow_normal, FontWeight.Normal, FontStyle.Normal),
            Font(Res.font.barlow_medium, FontWeight.Medium, FontStyle.Normal),
            Font(Res.font.barlow_bold, FontWeight.Bold, FontStyle.Normal),
        )

        copy(
            displayLarge = displayLarge.copy(fontFamily = fontFamily),
            displayMedium = displayMedium.copy(fontFamily = fontFamily),
            displaySmall = displaySmall.copy(fontFamily = fontFamily),
            headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
            headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
            headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
            titleLarge = titleLarge.copy(fontFamily = fontFamily),
            titleMedium = titleMedium.copy(fontFamily = fontFamily),
            titleSmall = titleSmall.copy(fontFamily = fontFamily),
            bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
            bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
            bodySmall = bodySmall.copy(fontFamily = fontFamily),
            labelLarge = labelLarge.copy(fontFamily = fontFamily),
            labelMedium = labelMedium.copy(fontFamily = fontFamily),
            labelSmall = labelSmall.copy(fontFamily = fontFamily),
        )
    }
