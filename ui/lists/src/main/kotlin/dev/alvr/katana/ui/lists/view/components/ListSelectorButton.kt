package dev.alvr.katana.ui.lists.view.components

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.alvr.katana.ui.lists.R

@Composable
internal fun ListSelectorButton(
    onClick: () -> Unit,
) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            imageVector = Icons.TwoTone.List,
            contentDescription = stringResource(R.string.list_selector),
        )
    }
}
