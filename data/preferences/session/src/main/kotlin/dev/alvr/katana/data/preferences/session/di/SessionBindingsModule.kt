package dev.alvr.katana.data.preferences.session.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.data.preferences.session.repositories.SessionRepositoryImpl
import dev.alvr.katana.data.preferences.session.sources.SessionLocalSource
import dev.alvr.katana.data.preferences.session.sources.SessionLocalSourceImpl
import dev.alvr.katana.domain.session.repositories.SessionRepository

@Module
@InstallIn(SingletonComponent::class)
internal sealed interface SessionBindingsModule {
    @Binds
    fun bindSessionRepository(impl: SessionRepositoryImpl): SessionRepository

    @Binds
    fun bindSessionLocalSource(impl: SessionLocalSourceImpl): SessionLocalSource
}
