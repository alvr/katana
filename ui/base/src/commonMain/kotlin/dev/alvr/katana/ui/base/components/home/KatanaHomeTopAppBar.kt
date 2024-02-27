package dev.alvr.katana.ui.base.components.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.ui.base.base.generated.resources.Res
import dev.alvr.katana.ui.base.base.generated.resources.toolbar_menu_filter
import dev.alvr.katana.ui.base.base.generated.resources.toolbar_menu_search
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalResourceApi::class,
)
fun KatanaHomeTopAppBar(
    title: String,
    subtitle: String?,
    modifier: Modifier = Modifier,
    onSearch: (() -> Unit)? = null,
    onFilter: (() -> Unit)? = null,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Column {
                Text(text = title)
                if (!subtitle.isNullOrBlank()) {
                    Text(text = subtitle, style = MaterialTheme.typography.titleSmall)
                }
            }
        },
        actions = {
            if (onSearch != null) {
                IconButton(onClick = onSearch) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = stringResource(Res.string.toolbar_menu_search),
                    )
                }
            }

            if (onFilter != null) {
                IconButton(onClick = onFilter) {
                    Icon(
                        imageVector = Icons.Outlined.FilterAlt,
                        contentDescription = stringResource(Res.string.toolbar_menu_filter),
                    )
                }
            }
        },
    )
}
