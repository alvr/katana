package dev.alvr.katana.data.preferences.token.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.data.preferences.token.repositories.TokenPreferencesRepositoryImpl
import dev.alvr.katana.domain.token.repositories.TokenPreferencesRepository

@Module
@InstallIn(SingletonComponent::class)
@Suppress("UnnecessaryAbstractClass")
internal abstract class TokenBindingsModule {
    @Binds
    abstract fun provideTokenPreferencesRepository(
        impl: TokenPreferencesRepositoryImpl
    ): TokenPreferencesRepository
}
