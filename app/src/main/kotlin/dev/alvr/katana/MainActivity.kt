package dev.alvr.katana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import dev.alvr.katana.navigation.KatanaNavigator
import dev.alvr.katana.ui.base.Katana

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Katana {
                ProvideWindowInsets {
                    KatanaNavigator()
                }
            }
        }
    }
}
