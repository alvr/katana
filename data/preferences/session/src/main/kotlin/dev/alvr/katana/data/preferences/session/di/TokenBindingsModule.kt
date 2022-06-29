package dev.alvr.katana.data.preferences.session.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.data.preferences.session.repositories.TokenPreferencesRepositoryImpl
import dev.alvr.katana.domain.session.repositories.TokenPreferencesRepository

@Module
@InstallIn(SingletonComponent::class)
internal sealed interface TokenBindingsModule {
    @Binds
    fun bindTokenPreferencesRepository(impl: TokenPreferencesRepositoryImpl): TokenPreferencesRepository
}
