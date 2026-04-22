package dev.alvr.katana.di

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.annotation.Keep
import androidx.core.app.AppComponentFactory
import dev.alvr.katana.KatanaApp
import dev.alvr.katana.MainActivity
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import kotlin.properties.Delegates
import kotlin.reflect.KClass

@Keep
internal class MetroAppComponentFactory : AppComponentFactory() {
    override fun instantiateActivityCompat(cl: ClassLoader, className: String, intent: Intent?): Activity {
        if (className == MainActivity::class.java.name) {
            return MainActivity(state.viewModelFactory)
        }

        return getInstanceOrNull<Activity>(cl, className, state.activityProviders) ?: super.instantiateActivityCompat(cl, className, intent)
    }

    override fun instantiateApplicationCompat(cl: ClassLoader, className: String): Application =
        super.instantiateApplicationCompat(cl, className).also { app ->
            val componentProviders = (app as KatanaApp).appComponentProviders
            state = ComponentState(
                activityProviders = componentProviders.activityProviders,
                viewModelFactory = componentProviders.metroViewModelFactory,
            )
        }

    private inline fun <reified T : Any> getInstanceOrNull(
        cl: ClassLoader,
        className: String,
        providers: Map<KClass<out T>, () -> T>,
    ): T? {
        val clazz = Class.forName(className, false, cl).asSubclass(T::class.java)
        return providers[clazz.kotlin]?.invoke()
    }
}

private data class ComponentState(
    val activityProviders: Map<KClass<out Activity>, () -> Activity>,
    val viewModelFactory: MetroViewModelFactory,
)

private var state by Delegates.notNull<ComponentState>()
