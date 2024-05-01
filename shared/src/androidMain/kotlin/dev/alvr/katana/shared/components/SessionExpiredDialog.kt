package dev.alvr.katana.shared.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.shared.resources.Res
import dev.alvr.katana.shared.resources.session_expired_error_confirm_button
import dev.alvr.katana.shared.resources.session_expired_error_message
import dev.alvr.katana.shared.resources.session_expired_error_title

@Composable
internal fun SessionExpiredDialog(
    visible: Boolean,
    onAccept: () -> Unit,
) {
    if (visible) {
        AlertDialog(
            onDismissRequest = {
                /* Nothing */
            },
            confirmButton = {
                TextButton(onClick = onAccept) {
                    Text(text = Res.string.session_expired_error_confirm_button.value)
                }
            },
            title = { Text(text = Res.string.session_expired_error_title.value) },
            text = { Text(text = Res.string.session_expired_error_message.value) },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            ),
        )
    }
}
