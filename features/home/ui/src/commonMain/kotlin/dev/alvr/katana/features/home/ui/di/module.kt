package dev.alvr.katana.features.home.ui.di

import dev.alvr.katana.features.home.ui.component.DefaultHomeComponent.Factory
import dev.alvr.katana.features.home.ui.component.HomeComponent
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val componentModule = module {
    factoryOf(::Factory) bind HomeComponent.Factory::class
}

val featuresHomeUiModule = module {
    includes(componentModule)
}
