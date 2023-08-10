package dev.alvr.katana.ui.lists.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import dev.alvr.katana.ui.lists.KR
import dev.alvr.katana.ui.lists.entities.UserList

@Composable
@Destination(style = DestinationStyleBottomSheet::class)
internal fun ChangeListSheet(
    lists: Array<UserList>,
    selectedList: String,
    resultNavigator: ResultBackNavigator<String>,
) {
    Column {
        lists.forEach { (name, count) ->
            Text(
                text = buildAnnotatedString {
                    append(name)
                    withStyle(
                        SpanStyle(
                            baselineShift = BaselineShift.Superscript,
                            fontSize = 12.sp,
                            color = MaterialTheme.colors.secondary,
                        ),
                    ) {
                        append(count.toString())
                    }
                },
                fontWeight = if (selectedList == name) FontWeight.SemiBold else FontWeight.Normal,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { resultNavigator.navigateBack(name) }
                    .height(48.dp)
                    .padding(all = 8.dp),
            )
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
                contentDescription = KR.string.lists_change_list_button,
            )
        }
    }
}
