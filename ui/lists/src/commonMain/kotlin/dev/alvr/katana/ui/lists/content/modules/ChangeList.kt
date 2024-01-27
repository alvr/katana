package dev.alvr.katana.ui.lists.content.modules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.alvr.katana.ui.lists.entities.UserList
import dev.alvr.katana.ui.lists.strings.LocalListsStrings

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun ChangeListSheet(
    lists: List<UserList>,
    selectedList: String,
    onResult: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        windowInsets = WindowInsets.navigationBars,
    ) {
        Column(
            modifier = Modifier.navigationBarsPadding(),
        ) {
            lists.forEach { (name, count) ->
                Text(
                    text = buildAnnotatedString {
                        append(name)
                        withStyle(
                            SpanStyle(
                                baselineShift = BaselineShift.Superscript,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                        ) {
                            append(" $count")
                        }
                    },
                    fontWeight = if (selectedList == name) FontWeight.SemiBold else FontWeight.Normal,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onResult(name)
                            onDismiss()
                        }
                        .height(48.dp)
                        .padding(all = 8.dp),
                )
            }
        }
    }
}

@Composable
internal fun ChangeListButton(
    visible: Boolean,
    onClick: () -> Unit,
) {
    if (visible) {
        FloatingActionButton(onClick = onClick) {
            Icon(
                imageVector = Icons.TwoTone.List,
                contentDescription = LocalListsStrings.current.changeListButton,
            )
        }
    }
}
