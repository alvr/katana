package dev.alvr.katana.ui.base.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Error
import androidx.compose.material.icons.twotone.Inbox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.alvr.katana.ui.base.base.generated.resources.Res
import dev.alvr.katana.ui.base.base.generated.resources.component_empty_state
import dev.alvr.katana.ui.base.base.generated.resources.component_error_state
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalResourceApi::class)
fun KatanaEmptyState(
    text: String,
    modifier: Modifier = Modifier,
) {
    KatanaState(
        modifier = modifier,
        text = text,
        imageVector = Icons.TwoTone.Inbox,
        contentDescription = stringResource(Res.string.component_empty_state),
    )
}

@Composable
@OptIn(ExperimentalResourceApi::class)
fun KatanaErrorState(
    text: String,
    loading: Boolean,
    buttonText: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    KatanaState(
        modifier = modifier,
        text = text,
        imageVector = Icons.TwoTone.Error,
        contentDescription = stringResource(Res.string.component_error_state),
    ) {
        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            modifier = Modifier.alpha(if (loading) RETRY_BUTTON_DISABLED_ALPHA else 1f),
            onClick = onRetry,
            enabled = !loading,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = contentColorFor(MaterialTheme.colorScheme.primary),
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
