package dev.alvr.katana.core.ui.decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ComponentContextFactory
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import kotlin.properties.Delegates
import org.koin.core.Koin
import org.koin.core.component.getOrCreateScope
import org.koin.core.component.getScopeId
import org.koin.core.scope.Scope

@PublishedApi
internal class DefaultAppComponentContext(
    componentContext: AppComponentContext,
) : AppComponentContext by componentContext {
    override val scope by getOrCreateScope()

    override val componentContextFactory =
        ComponentContextFactory { lifecycle, stateKeeper, instanceKeeper, backHandler ->
            val ctx = componentContext.componentContextFactory(
                lifecycle = lifecycle,
                stateKeeper = stateKeeper,
                instanceKeeper = instanceKeeper,
                backHandler = backHandler,
            )
            DefaultAppComponentContext(ctx)
        }

    init {
        doOnDestroy { scope.close() }
    }
}
