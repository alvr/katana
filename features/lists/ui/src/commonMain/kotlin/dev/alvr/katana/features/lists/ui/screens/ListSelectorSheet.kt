package dev.alvr.katana.features.lists.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import dev.alvr.katana.core.ui.navigation.BottomSheetSceneStrategy
import dev.alvr.katana.core.ui.navigation.KatanaEntryProviderScope
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.symbols.KatanaSymbols
import dev.alvr.katana.core.ui.symbols.Lists
import dev.alvr.katana.core.ui.theme.KatanaTheme
import dev.alvr.katana.features.lists.ui.entities.UserList
import dev.alvr.katana.features.lists.ui.navigation.ListsDestination
import dev.alvr.katana.features.lists.ui.resources.Res
import dev.alvr.katana.features.lists.ui.resources.change_list_button
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
internal fun KatanaEntryProviderScope.listSelectorSheet() {
    entry<ListsDestination.Selector>(metadata = BottomSheetSceneStrategy.bottomSheet()) { destination ->
        // val result = LocalResultEventBus.current

        ListSelectorSheet(
            selected = destination.selected,
            lists = destination.lists,
            onClick = { list ->
                // result.sendResult(list)
            },
        )
    }
}

@Composable
private fun ListSelectorSheet(selected: String, lists: ImmutableList<UserList>, onClick: (String) -> Unit) {
    lists.forEach { (name, count) ->
        Text(
            text =
                buildAnnotatedString {
                    append(name)
                    withStyle(
                        SpanStyle(
                            baselineShift = BaselineShift.Superscript,
                            fontSize = 12.sp,
                            color = KatanaTheme.colorScheme.onSurfaceVariant,
                        )
                    ) {
                        append(" $count")
                    }
                },
            fontWeight = if (selected == name) FontWeight.SemiBold else FontWeight.Normal,
            style = KatanaTheme.typography.titleLarge,
            modifier =
                Modifier.fillMaxWidth()
                    .clickable { onClick(name) }
                    .heightIn(min = KatanaTheme.dimensions.spacing12)
                    .padding(all = KatanaTheme.dimensions.spacing2),
        )
    }
}

@Composable
internal fun ChangeListButton(visible: Boolean, onClick: () -> Unit) {
    if (visible) {
        FloatingActionButton(onClick = onClick) {
            Icon(contentDescription = Res.string.change_list_button.value, imageVector = KatanaSymbols.Lists)
        }
    }
}
