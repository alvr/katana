package dev.alvr.katana.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import dev.alvr.katana.ui.base.design.Katana
import dev.alvr.katana.ui.main.navigation.KatanaDestinations

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Katana {
                KatanaDestinations()
            }
        }
    }
}
