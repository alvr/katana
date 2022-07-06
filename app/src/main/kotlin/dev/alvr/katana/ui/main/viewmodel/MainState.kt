package dev.alvr.katana.ui.main.viewmodel

import androidx.compose.runtime.Stable
import com.ramcosta.composedestinations.spec.NavGraphSpec

@Stable
internal data class MainState(
    val initialNavGraph: NavGraphSpec,
    val isSessionActive: Boolean = true,
)
