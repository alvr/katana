package dev.alvr.katana.shared.viewmodel

import com.ramcosta.composedestinations.spec.NavGraphSpec

internal data class MainState(
    val initialNavGraph: NavGraphSpec,
    val isSessionActive: Boolean = true,
)
