package dev.alvr.katana.features.home.ui.screens.activity.viewmodel

import dev.alvr.katana.core.ui.viewmodel.UiIntent

internal sealed interface ActivityIntent : UiIntent {
    companion object : ActivityIntent // TODO: remove when adding first intent
}
