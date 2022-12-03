package dev.alvr.katana.ui.lists.view.edit

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyle

@Composable
@Destination(style = DestinationStyle.BottomSheet::class)
internal fun EditEntrySheet() {
    Text(text = "EditEntrySheet")
}
