package dev.alvr.katana.core.ui.components.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import dev.alvr.katana.core.ui.components.KatanaSnackbarVisuals
import dev.alvr.katana.core.ui.components.showSnackbar
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.channels.Channel
import org.jetbrains.compose.resources.StringResource

@Stable
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class SnackbarControllerImpl : SnackbarController {
    private val messages = Channel<KatanaSnackbarVisuals>(Channel.BUFFERED)

    override suspend fun showMessage(
        message: StringResource,
        actionLabel: StringResource?,
        withDismissAction: Boolean,
        duration: SnackbarDuration,
        onAction: (() -> Unit)?,
        onDismiss: (() -> Unit)?,
    ) {
        messages.send(
            KatanaSnackbarVisuals(
                messageResource = message,
                actionLabelResource = actionLabel,
                withDismissAction = withDismissAction,
                duration = duration,
                onAction = onAction,
                onDismiss = onDismiss,
            )
        )
    }

    override fun tryShowMessage(
        message: StringResource,
        actionLabel: StringResource?,
        withDismissAction: Boolean,
        duration: SnackbarDuration,
        onAction: (() -> Unit)?,
        onDismiss: (() -> Unit)?,
    ) {
        messages.trySend(
            KatanaSnackbarVisuals(
                messageResource = message,
                actionLabelResource = actionLabel,
                withDismissAction = withDismissAction,
                duration = duration,
                onAction = onAction,
                onDismiss = onDismiss,
            )
        )
    }

    @Composable
    context(snackbarHostState: SnackbarHostState)
    override fun SnackbarMessageHandler() {
        LaunchedEffect(this, snackbarHostState) {
            for (message in messages) {
                snackbarHostState.showSnackbar(
                    messageResource = message.messageResource,
                    actionLabelResource = message.actionLabelResource,
                    withDismissAction = message.withDismissAction,
                    duration = message.duration,
                    onAction = message.onAction,
                    onDismiss = message.onDismiss,
                )
            }
        }
    }
}
