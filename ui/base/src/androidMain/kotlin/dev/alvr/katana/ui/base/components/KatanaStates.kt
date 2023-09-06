package dev.alvr.katana.ui.base.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Error
import androidx.compose.material.icons.twotone.Inbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.alvr.katana.ui.base.KR

@Composable
fun KatanaEmptyState(
    text: String,
    modifier: Modifier = Modifier,
) {
    KatanaState(
        modifier = modifier,
        text = text,
        imageVector = Icons.TwoTone.Inbox,
        contentDescription = KR.string.component_empty_state,
    )
}

@Composable
fun KatanaErrorState(
    text: String,
    loading: Boolean,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    buttonText: String = KR.string.component_error_state_retry_button,
) {
    KatanaState(
        modifier = modifier,
        text = text,
        imageVector = Icons.TwoTone.Error,
        contentDescription = KR.string.component_error_state,
    ) {
        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            modifier = Modifier.alpha(if (loading) RETRY_BUTTON_DISABLED_ALPHA else 1f),
            onClick = onRetry,
            enabled = !loading,
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = MaterialTheme.colors.secondaryVariant,
                contentColor = contentColorFor(MaterialTheme.colors.primaryVariant),
            ),
        ) {
            Text(text = buttonText, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun KatanaState(
    text: String,
    imageVector: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    extraContent: @Composable (() -> Unit)? = null,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(STATE_CONTENT_FRACTION),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = imageVector,
                modifier = Modifier.size(160.dp),
                contentDescription = contentDescription,
            )
            Text(
                modifier = Modifier,
                text = text,
                textAlign = TextAlign.Justify,
            )

            extraContent?.invoke()
        }
    }
}

private const val STATE_CONTENT_FRACTION = .9f
private const val RETRY_BUTTON_DISABLED_ALPHA = .6f
