package dev.alvr.katana.core.ui.resources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

val DrawableResource.asPainter
    @Composable
    get() = painterResource(this)

val StringResource.value
    @Composable
    get() = stringResource(this)

@Composable
fun StringResource.format(vararg format: Any) = stringResource(this, *format)
