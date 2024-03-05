package dev.alvr.katana.common.session.data.di

import dev.alvr.katana.common.session.data.repositories.SessionRepositoryImpl
import dev.alvr.katana.common.session.data.sources.SessionLocalSource
import dev.alvr.katana.common.session.data.sources.SessionLocalSourceImpl
import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.common.di.ApplicationScope
import me.tatarka.inject.annotations.Provides

expect interface PlatformCommonSessionDataComponent

interface CommonSessionDataComponent : PlatformCommonSessionDataComponent {

    @Provides
    @ApplicationScope
    fun provideSessionRepository(impl: SessionRepositoryImpl): SessionRepository = impl

    @Provides
    @ApplicationScope
    fun provideSessionLocalSource(impl: SessionLocalSourceImpl): SessionLocalSource = impl
}
