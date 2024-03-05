package dev.alvr.katana.shared.di

import android.app.Activity
import dev.alvr.katana.core.common.di.ScreenScope
import dev.alvr.katana.shared.Katana
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@ScreenScope
abstract class AndroidActivityComponent(
    @get:Provides val activity: Activity,
    @Component val applicationComponent: AndroidApplicationComponent,
) : SharedUiComponent {
    abstract val app: Katana

    companion object
}
