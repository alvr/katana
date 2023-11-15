package dev.alvr.katana.main.di

import android.app.Application
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dev.alvr.katana.data.preferences.base.di.dataPreferencesBaseModule
import dev.alvr.katana.data.preferences.session.di.dataPreferencesSessionModule
import dev.alvr.katana.data.remote.account.di.dataRemoteAccountModule
import dev.alvr.katana.data.remote.base.di.dataRemoteBaseModule
import dev.alvr.katana.data.remote.explore.di.dataRemoteExploreModule
import dev.alvr.katana.data.remote.lists.di.dataRemoteListsModule
import dev.alvr.katana.data.remote.social.di.dataRemoteSocialModule
import dev.alvr.katana.data.remote.user.di.dataRemoteUserModule
import dev.alvr.katana.domain.account.di.domainAccountModule
import dev.alvr.katana.domain.explore.di.domainExploreModule
import dev.alvr.katana.domain.lists.di.domainListsModule
import dev.alvr.katana.domain.session.di.domainSessionModule
import dev.alvr.katana.domain.social.di.domainSocialModule
import dev.alvr.katana.domain.user.di.domainUserModule
import dev.alvr.katana.main.viewmodel.MainViewModel
import dev.alvr.katana.ui.account.di.uiAccountModule
import dev.alvr.katana.ui.explore.di.uiExploreModule
import dev.alvr.katana.ui.lists.di.uiListsModule
import dev.alvr.katana.ui.login.di.uiLoginModule
import dev.alvr.katana.ui.social.di.uiSocialModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.binds
import org.koin.dsl.module

@Composable
internal fun platformModule(): Module {
    val applicationContext = LocalContext.current.applicationContext

    return module {
        single { applicationContext } binds arrayOf(Application::class, Context::class)
    }
}

private val uiMainModule = module {
    viewModelOf(::MainViewModel)
}

internal val katanaModule = module {
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
