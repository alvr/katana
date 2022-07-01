package dev.alvr.katana.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import dev.alvr.katana.R

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
                    Text(text = stringResource(id = R.string.session_expired_error_confirm_button))
                }
            },
            title = { Text(text = stringResource(id = R.string.session_expired_error_title)) },
            text = { Text(text = stringResource(id = R.string.session_expired_error_message)) },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            ),
        )
    }
}
