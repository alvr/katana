package dev.alvr.katana.ui.base.components.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterialApi
fun KatanaHomeScaffold(
    @StringRes title: Int,
    subtitle: String? = null,
    backContent: @Composable () -> Unit,
    fab: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    val coroutineScope = rememberCoroutineScope()

    BackdropScaffold(
        scaffoldState = scaffoldState,
        gesturesEnabled = false,
        appBar = {
            KatanaTopAppBar(
                title = stringResource(title),
                subtitle = subtitle,
                onFilter = { scaffoldState.toggleBackdrop(coroutineScope) },
            )
        },
        backLayerContent = backContent,
        frontLayerShape = RectangleShape,
        frontLayerContent = {
            Scaffold(
                floatingActionButton = fab,
                content = content,
            )
        },
    )
}

@ExperimentalMaterialApi
private fun BackdropScaffoldState.toggleBackdrop(scope: CoroutineScope) {
    scope.launch {
        if (isConcealed) {
            reveal()
        } else {
            conceal()
        }
    }
}
