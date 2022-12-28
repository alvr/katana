package dev.alvr.katana.ui.lists.view.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.CalendarMonth
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun DateSelector(
    date: String,
    onOpen: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface,
                shape = MaterialTheme.shapes.small,
            )
            .clickable(onClick = onOpen)
            .padding(12.dp),
    ) {
        Icon(
            imageVector = Icons.TwoTone.CalendarMonth,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(8.dp))

        Text(text = date)

        if (date.length > 1) { // No date is represented as `-`
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier.clickable(onClick = onClear),
                imageVector = Icons.TwoTone.Clear,
                contentDescription = null,
            )
        }
    }
}
