package dev.alvr.katana.features.social.ui.di

import dev.alvr.katana.features.social.ui.viewmodel.SocialViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private val viewModelsModule = module {
    viewModelOf(::SocialViewModel)
}

val featuresSocialUiModule = module {
    includes(viewModelsModule)
}
