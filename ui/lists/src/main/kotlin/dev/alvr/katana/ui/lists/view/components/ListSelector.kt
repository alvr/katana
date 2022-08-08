package dev.alvr.katana.ui.lists.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle

@Composable
@Destination(style = DestinationStyle.BottomSheet::class)
internal fun ListSelector(
    lists: Array<String>,
    resultNavigator: ResultBackNavigator<String>,
) {
    Column {
        lists.forEach { name ->
            Text(
                text = name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { resultNavigator.navigateBack(name) }
                    .padding(all = 8.dp),
            )
        }
    }
}
