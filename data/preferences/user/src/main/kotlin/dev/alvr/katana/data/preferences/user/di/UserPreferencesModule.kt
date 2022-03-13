package dev.alvr.katana.data.preferences.user.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.data.preferences.user.sources.UserPreferencesSourceImpl
import dev.alvr.katana.domain.user.sources.UserPreferencesSource

@Module
@InstallIn(SingletonComponent::class)
internal object UserPreferencesModule {
    @Provides
    fun provideUserPreferencesSource(
        impl: UserPreferencesSourceImpl
    ): UserPreferencesSource = impl
}
