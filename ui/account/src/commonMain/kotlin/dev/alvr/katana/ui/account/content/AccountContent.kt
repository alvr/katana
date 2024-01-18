package dev.alvr.katana.ui.account.content

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alvr.katana.ui.account.component.AccountComponent
import dev.alvr.katana.ui.base.design.noInsets

@Composable
fun AccountContent(
    component: AccountComponent,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.noInsets,
    ) { paddingValues ->
        Text("AccountContent")
    }
}
