package dev.alvr.katana.ui.main.di

import androidx.compose.runtime.Composable
import org.koin.core.module.Module

@Composable
internal expect fun platformModule(): Module

internal expect val katanaModule: Module
