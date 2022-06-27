package dev.alvr.katana.data.preferences.token.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.data.preferences.token.repositories.TokenPreferencesRepositoryImpl
import dev.alvr.katana.domain.token.repositories.TokenPreferencesRepository

@Module
@InstallIn(SingletonComponent::class)
internal sealed interface TokenBindingsModule {
    @Binds
    fun bindTokenPreferencesRepository(impl: TokenPreferencesRepositoryImpl): TokenPreferencesRepository
}
