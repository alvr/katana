package dev.alvr.katana.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import dagger.hilt.android.AndroidEntryPoint
import dev.alvr.katana.ui.base.design.Katana
import dev.alvr.katana.ui.main.navigation.KatanaDestinations
import dev.alvr.katana.ui.main.viewmodel.MainViewModel

@AndroidEntryPoint
@ExperimentalMaterial3WindowSizeClassApi
internal class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            val useNavRail = windowSizeClass.widthSizeClass > WindowWidthSizeClass.Medium

            Katana {
                KatanaDestinations(
                    useNavRail = useNavRail,
                    vm = viewModel,
                )
            }
        }
    }
}
