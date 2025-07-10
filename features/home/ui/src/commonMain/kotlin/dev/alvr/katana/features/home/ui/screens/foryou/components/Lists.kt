package dev.alvr.katana.features.home.ui.screens.foryou.components

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.alvr.katana.core.ui.theme.KatanaTheme

@Composable
internal fun Lists(
    title: String,
    iconButtonContentDescription: String,
    onNavigateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(KatanaTheme.dimensions.spacing2),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = KatanaTheme.typography.headlineSmall,
            )

            Spacer(Modifier.weight(1f))

            IconButton(onClick = onNavigateClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = iconButtonContentDescription,
                )
            }
        }

        LazyRow(
            state = lazyListState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState, SnapPosition.Start),
            horizontalArrangement = Arrangement.spacedBy(KatanaTheme.dimensions.itemSpacing),
        ) {
        }
    }
}
