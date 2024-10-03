package dev.alvr.katana.features.lists.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.List
import androidx.compose.material.navigation.bottomSheet
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.core.bundle.bundleOf
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.features.lists.ui.destinations.ListsDestination
import dev.alvr.katana.features.lists.ui.entities.UserList
import dev.alvr.katana.features.lists.ui.navigation.ListsNavigator
import dev.alvr.katana.features.lists.ui.resources.Res
import dev.alvr.katana.features.lists.ui.resources.change_list_button
import kotlinx.serialization.json.Json

internal fun NavGraphBuilder.changeListBottomSheet(navigator: ListsNavigator) {
    bottomSheet(
        route = ListsDestination.ChangeList.ROUTE,
        arguments = listOf(
            navArgument("lists") { type = NavType.StringType },
            navArgument("selectedList") { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val arguments = backStackEntry.arguments ?: bundleOf()
        val lists = Json.decodeFromString<List<UserList>>(arguments.getString("lists").orEmpty())
        val selectedList = arguments.getString("selectedList").orEmpty()

        ChangeListSheet(
            lists = lists,
            selectedList = selectedList,
            onClick = navigator::popBackStackWithResult,
        )
    }
}

@Composable
private fun ChangeListSheet(
    lists: List<UserList>,
    selectedList: String,
    onClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
    ) {
        lists.forEach { (name, count) ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(name) }
                    .defaultMinSize(minHeight = 48.dp)
                    .padding(all = 8.dp)
                    .wrapContentHeight(),
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
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = if (selectedList == name) FontWeight.SemiBold else FontWeight.Normal,
                style = MaterialTheme.typography.titleLarge,
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
                contentDescription = Res.string.change_list_button.value,
                imageVector = Icons.AutoMirrored.TwoTone.List,
            )
        }
    }
}
