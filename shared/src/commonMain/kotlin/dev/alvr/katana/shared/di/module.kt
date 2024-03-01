package dev.alvr.katana.shared.di

import dev.alvr.katana.common.session.data.di.dataPreferencesSessionModule
import dev.alvr.katana.common.session.domain.di.domainSessionModule
import dev.alvr.katana.common.user.data.di.dataRemoteUserModule
import dev.alvr.katana.common.user.domain.domainUserModule
import dev.alvr.katana.core.preferences.di.dataPreferencesBaseModule
import dev.alvr.katana.core.remote.di.dataRemoteBaseModule
import dev.alvr.katana.features.account.data.di.dataRemoteAccountModule
import dev.alvr.katana.features.account.domain.di.domainAccountModule
import dev.alvr.katana.features.account.ui.di.uiAccountModule
import dev.alvr.katana.features.explore.data.di.dataRemoteExploreModule
import dev.alvr.katana.features.explore.domain.di.domainExploreModule
import dev.alvr.katana.features.explore.ui.di.uiExploreModule
import dev.alvr.katana.features.lists.data.di.dataRemoteListsModule
import dev.alvr.katana.features.lists.domain.di.domainListsModule
import dev.alvr.katana.features.lists.ui.di.uiListsModule
import dev.alvr.katana.features.login.ui.di.uiLoginModule
import dev.alvr.katana.features.social.data.di.dataRemoteSocialModule
import dev.alvr.katana.features.social.domain.di.domainSocialModule
import dev.alvr.katana.features.social.ui.di.uiSocialModule
import dev.alvr.katana.shared.viewmodel.MainViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val uiMainModule = module {
    factoryOf(::MainViewModel)
}

val katanaModule = module {
    includes(
        // Domain
        domainAccountModule,
        domainExploreModule,
        domainListsModule,
        domainSessionModule,
        domainSocialModule,
        domainUserModule,

        // Data Preferences
        dataPreferencesBaseModule,
        dataPreferencesSessionModule,

        // Data Remote
        dataRemoteAccountModule,
        dataRemoteExploreModule,
        dataRemoteBaseModule,
        dataRemoteListsModule,
        dataRemoteSocialModule,
        dataRemoteUserModule,

        // Ui
        uiAccountModule,
        uiExploreModule,
        uiListsModule,
        uiLoginModule,
        uiMainModule,
        uiSocialModule,
    )
}
