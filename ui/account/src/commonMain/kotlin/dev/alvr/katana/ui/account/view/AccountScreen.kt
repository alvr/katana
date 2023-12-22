package dev.alvr.katana.ui.account.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.ui.account.strings.LocalAccountStrings
import dev.alvr.katana.ui.base.navigation.Destination

@Composable
@Destination
internal fun AccountScreen() {
    Scaffold { paddingValues ->
        Text(
            text = LocalAccountStrings.current.title,
            modifier = Modifier.padding(paddingValues),
        )
    }
}
