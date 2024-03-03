package dev.alvr.katana.shared.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import dev.alvr.katana.shared.R

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
                    Text(text = stringResource(R.string.session_expired_error_confirm_button))
                }
            },
            title = { Text(text = stringResource(R.string.session_expired_error_title)) },
            text = { Text(text = stringResource(R.string.session_expired_error_message)) },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            ),
        )
    }
}
