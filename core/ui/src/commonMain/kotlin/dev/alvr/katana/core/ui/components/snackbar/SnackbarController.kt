package dev.alvr.katana.core.ui.components.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import org.jetbrains.compose.resources.StringResource

@Stable
interface SnackbarController {
    suspend fun showMessage(
        message: StringResource,
        actionLabel: StringResource? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
        onAction: (() -> Unit)? = null,
        onDismiss: (() -> Unit)? = null,
    )

    fun tryShowMessage(
        message: StringResource,
        actionLabel: StringResource? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
        onAction: (() -> Unit)? = null,
        onDismiss: (() -> Unit)? = null,
    )

    @Composable
    context(snackbarHostState: SnackbarHostState)
    fun SnackbarMessageHandler()
}

val LocalSnackbarController = staticCompositionLocalOf<SnackbarController> { error("No SnackbarController provided") }
