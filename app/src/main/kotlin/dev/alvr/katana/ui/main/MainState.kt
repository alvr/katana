package dev.alvr.katana.ui.main

import androidx.compose.runtime.Stable
import com.ramcosta.composedestinations.spec.NavGraphSpec

@Stable
internal data class MainState(
    val initialNavGraph: NavGraphSpec,
)
