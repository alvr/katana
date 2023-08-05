package dev.alvr.katana.ui.base.design

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource

private val cache = mutableMapOf<String, Font>()

@Composable
@OptIn(ExperimentalResourceApi::class)
internal actual fun font(font: String, weight: FontWeight, style: FontStyle) =
    cache.getOrPut(font) {
        val byteArray = runBlocking { resource("font/$font.ttf").readBytes() }
        Font(font, byteArray, weight, style)
    }
