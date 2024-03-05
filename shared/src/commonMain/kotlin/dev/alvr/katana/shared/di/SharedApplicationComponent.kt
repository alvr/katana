package dev.alvr.katana.shared.di

import dev.alvr.katana.common.session.data.di.CommonSessionDataComponent
import dev.alvr.katana.common.user.data.di.CommonUserDataComponent
import dev.alvr.katana.core.preferences.di.CorePreferencesComponent
import dev.alvr.katana.core.remote.di.CoreRemoteComponent

interface SharedApplicationComponent :
    CommonSessionDataComponent,
    CommonUserDataComponent,
    CorePreferencesComponent,
    CoreRemoteComponent
