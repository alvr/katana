package dev.alvr.katana.core.preferences.di

import dev.alvr.katana.core.common.di.ApplicationScope
import dev.alvr.katana.core.preferences.encrypt.IosPreferencesEncrypt
import dev.alvr.katana.core.preferences.encrypt.PreferencesEncrypt
import me.tatarka.inject.annotations.Provides

actual sealed interface PlatformCorePreferencesComponent {

    @Provides
    @ApplicationScope
    fun providePreferencesEncrypt(impl: IosPreferencesEncrypt): PreferencesEncrypt = impl
}
