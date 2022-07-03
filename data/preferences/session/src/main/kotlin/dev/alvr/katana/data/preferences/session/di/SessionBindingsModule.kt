package dev.alvr.katana.data.preferences.session.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.data.preferences.session.repositories.SessionPreferencesRepositoryImpl
import dev.alvr.katana.domain.session.repositories.SessionPreferencesRepository

@Module
@InstallIn(SingletonComponent::class)
internal sealed interface SessionBindingsModule {
    @Binds
    fun bindSessionPreferencesRepository(impl: SessionPreferencesRepositoryImpl): SessionPreferencesRepository
}
