package dev.alvr.katana.ui.main.di

import dev.alvr.katana.data.preferences.base.di.baseDataPreferencesModule
import dev.alvr.katana.data.preferences.session.di.sessionDataPreferencesModule
import dev.alvr.katana.data.remote.base.di.baseDataRemoteModule
import dev.alvr.katana.data.remote.lists.di.listsDataRemoteModule
import dev.alvr.katana.data.remote.user.di.userDataRemoteModule
import dev.alvr.katana.domain.lists.di.listsDomainModule
import dev.alvr.katana.domain.session.di.sessionDomainModule
import dev.alvr.katana.domain.user.di.userDomainModule
import dev.alvr.katana.ui.lists.di.listsUiModule
import dev.alvr.katana.ui.login.di.loginUiModule
import dev.alvr.katana.ui.main.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

private val appUiModule = module {
    viewModelOf(::MainViewModel)
}

val katanaModule = module {
    includes(
        // Domain
        listsDomainModule,
        sessionDomainModule,
        userDomainModule,

        // Data Preferences
        baseDataPreferencesModule,
        sessionDataPreferencesModule,

        // Data Remote
        baseDataRemoteModule,
        listsDataRemoteModule,
        userDataRemoteModule,

        // Ui
        appUiModule,
        listsUiModule,
        loginUiModule,
    )
}
