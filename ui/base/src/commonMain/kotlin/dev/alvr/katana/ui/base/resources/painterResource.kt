package dev.alvr.katana.ui.base.resources

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
@OptIn(ExperimentalResourceApi::class)
fun painterResource(resource: KatanaResource) = painterResource(resource.key)
