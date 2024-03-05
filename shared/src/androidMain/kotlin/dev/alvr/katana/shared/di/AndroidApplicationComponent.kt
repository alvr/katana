package dev.alvr.katana.shared.di

import android.content.Context
import dev.alvr.katana.core.common.di.ApplicationScope
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@ApplicationScope
abstract class AndroidApplicationComponent(
    @get:Provides val context: Context
) : SharedApplicationComponent {
    companion object
}
