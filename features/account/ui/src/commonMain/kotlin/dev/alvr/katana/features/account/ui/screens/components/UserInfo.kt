package dev.alvr.katana.features.account.ui.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import dev.alvr.katana.core.ui.resources.value
import dev.alvr.katana.core.ui.theme.KatanaTheme
import dev.alvr.katana.features.account.ui.entities.UserInfoUi
import dev.alvr.katana.features.account.ui.resources.Res
import dev.alvr.katana.features.account.ui.resources.logout_button

@Composable
internal fun UserInfo(userInfo: UserInfoUi, onLogoutClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        UsernameAvatar(avatar = userInfo.avatar, username = userInfo.username)

        Button(onClick = onLogoutClick) { Text(text = Res.string.logout_button.value) }
    }
}

@Composable
private fun UsernameAvatar(avatar: String, username: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            modifier = Modifier.size(KatanaTheme.sizes.size18).clip(CircleShape),
            model = avatar,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        Text(text = username)
    }
}
