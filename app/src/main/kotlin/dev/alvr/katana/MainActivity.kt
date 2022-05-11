package dev.alvr.katana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import dev.alvr.katana.navigation.KatanaNavigator
import dev.alvr.katana.ui.base.Katana

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Katana {
                KatanaNavigator()
            }
        }
    }
}
