package dev.alvr.katana.shared.di

import dev.alvr.katana.shared.Katana
import dev.zacsweers.metrox.viewmodel.ViewModelGraph

interface KatanaGraph : ViewModelGraph {
    val app: Katana
}
