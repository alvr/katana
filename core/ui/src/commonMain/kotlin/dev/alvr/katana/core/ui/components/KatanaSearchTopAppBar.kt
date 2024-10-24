package dev.alvr.katana.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import dev.alvr.katana.core.common.empty

@Composable
fun KatanaSearchTopAppBar(
    searchPlaceholder: String,
    closeContentDescription: String,
    clearContentDescription: String,
    onValueChange: (String) -> Unit,
    onBack: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var focusRequested by rememberSaveable { mutableStateOf(false) }
    var search by rememberSaveable { mutableStateOf(String.empty) }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    // Request focus only the first time
    if (!focusRequested) {
        LaunchedEffect(Unit) {
            focusRequested = true
            focusRequester.requestFocus()
        }
    }

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = search,
        onValueChange = {
            search = it
            onValueChange(it)
        },
        placeholder = { Text(text = searchPlaceholder) },
        leadingIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    contentDescription = closeContentDescription,
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                )
            }
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    search = String.empty
                    onClear()
                },
            ) {
                Icon(
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = clearContentDescription,
                )
            }
        },
        singleLine = true,
        shape = RectangleShape,
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        colors = TextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = BackgroundOpacity),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
    )
}

private const val BackgroundOpacity = 0.12f
