package dev.alvr.katana.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import dagger.hilt.android.AndroidEntryPoint
import dev.alvr.katana.ui.base.design.Katana
import dev.alvr.katana.ui.main.navigation.KatanaDestinations

@AndroidEntryPoint
@ExperimentalMaterial3WindowSizeClassApi
internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            val useNavRail = windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact

            Katana {
                KatanaDestinations(useNavRail = useNavRail)
            }
        }
    }
}
