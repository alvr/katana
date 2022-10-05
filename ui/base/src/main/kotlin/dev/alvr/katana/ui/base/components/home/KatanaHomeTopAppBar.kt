package dev.alvr.katana.ui.base.components.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.alvr.katana.common.core.zero
import dev.alvr.katana.ui.base.R

@Composable
fun KatanaTopAppBar(
    title: String,
    subtitle: String?,
    onBack: (() -> Unit)? = null,
    onSearch: (() -> Unit)? = null,
    onFilter: (() -> Unit)? = null,
) {
    Column(modifier = Modifier.height(HEADER_HEIGHT.dp)) {
        TopAppBar(
            title = {
                Column {
                    Text(text = title)
                    if (!subtitle.isNullOrBlank()) {
                        Text(text = subtitle, style = MaterialTheme.typography.caption)
                    }
                }
            },
            navigationIcon = onBack?.display,
            actions = {
                if (onSearch != null) {
                    IconButton(onClick = onSearch) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = stringResource(R.string.menu_search),
                        )
                    }
                }

                if (onFilter != null) {
                    IconButton(onClick = onFilter) {
                        Icon(
                            imageVector = Icons.Outlined.FilterAlt,
                            contentDescription = stringResource(R.string.menu_filter),
                        )
                    }
                }
            },
            elevation = Int.zero.dp,
        )
    }
}

private val (() -> Unit).display: @Composable (() -> Unit)
    get() = {
        IconButton(onClick = this) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = "",
            )
        }
    }

private const val HEADER_HEIGHT = 56
