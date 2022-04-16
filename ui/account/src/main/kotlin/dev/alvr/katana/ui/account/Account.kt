package dev.alvr.katana.ui.account

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun Account() {
    Scaffold { paddingValues ->
        Text(
            text = stringResource(id = R.string.title),
            modifier = Modifier.padding(paddingValues),
        )
    }
}
