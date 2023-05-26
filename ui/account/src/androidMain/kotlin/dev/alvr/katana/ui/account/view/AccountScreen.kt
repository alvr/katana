package dev.alvr.katana.ui.account.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import dev.alvr.katana.ui.account.R

@Composable
@Destination
internal fun AccountScreen() {
    Scaffold { paddingValues ->
        Text(
            text = stringResource(R.string.title),
            modifier = Modifier.padding(paddingValues),
        )
    }
}