package dev.alvr.katana.shared.di

import dev.alvr.katana.common.session.data.di.commonSessionDataModule
import dev.alvr.katana.common.session.domain.di.commonSessionDomainModule
import dev.alvr.katana.common.user.data.di.commonUserDataModule
import dev.alvr.katana.common.user.domain.di.commonUserDomainModule
import dev.alvr.katana.core.common.di.coreCommonModule
import dev.alvr.katana.core.preferences.di.corePreferencesModule
import dev.alvr.katana.core.remote.di.coreRemoteModule
import dev.alvr.katana.features.account.data.di.featuresAccountDataModule
import dev.alvr.katana.features.account.domain.di.featuresAccountDomainModule
import dev.alvr.katana.features.account.ui.di.featuresAccountUiModule
import dev.alvr.katana.features.explore.data.di.featuresExploreDataModule
import dev.alvr.katana.features.explore.domain.di.featuresExploreDomainModule
import dev.alvr.katana.features.explore.ui.di.featuresExploreUiModule
import dev.alvr.katana.features.home.ui.di.featuresHomeUiModule
import dev.alvr.katana.features.lists.data.di.featuresListsDataModule
import dev.alvr.katana.features.lists.domain.di.featuresListsDomainModule
import dev.alvr.katana.features.lists.ui.di.featuresListsUiModule
import dev.alvr.katana.features.login.ui.di.featuresLoginUiModule
import dev.alvr.katana.features.social.data.di.featuresSocialDataModule
import dev.alvr.katana.features.social.domain.di.featuresSocialDomainModule
import dev.alvr.katana.features.social.ui.di.featuresSocialUiModule
import dev.alvr.katana.shared.viewmodel.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private val viewModelsModule = module {
    viewModelOf(::MainViewModel)
}

val katanaModule = module {
    includes(
        // Core
        coreCommonModule,
        corePreferencesModule,
        coreRemoteModule,

        // Common Session
        commonSessionDataModule,
        commonSessionDomainModule,

        // Common User
        commonUserDataModule,
        commonUserDomainModule,

        // Feature Account
        featuresAccountDataModule,
        featuresAccountDomainModule,
        featuresAccountUiModule,

        // Feature Explore
        featuresExploreDataModule,
        featuresExploreDomainModule,
        featuresExploreUiModule,

        // Feature Home
        featuresHomeUiModule,

        // Feature Lists
        featuresListsDataModule,
        featuresListsDomainModule,
        featuresListsUiModule,

        // Feature Login
        featuresLoginUiModule,

        // Feature Social
        featuresSocialDataModule,
        featuresSocialDomainModule,
        featuresSocialUiModule,

        // Shared
        viewModelsModule,
    )
}
