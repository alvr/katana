package dev.alvr.katana.common.user.data.di

import dev.alvr.katana.common.user.data.managers.UserIdManagerImpl
import dev.alvr.katana.common.user.data.repositories.UserRepositoryImpl
import dev.alvr.katana.common.user.data.sources.id.UserIdRemoteSource
import dev.alvr.katana.common.user.data.sources.id.UserIdRemoteSourceImpl
import dev.alvr.katana.common.user.data.sources.info.UserInfoRemoteSource
import dev.alvr.katana.common.user.data.sources.info.UserInfoRemoteSourceImpl
import dev.alvr.katana.common.user.domain.managers.UserIdManager
import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.alvr.katana.core.common.di.ApplicationScope
import me.tatarka.inject.annotations.Provides

interface CommonUserDataComponent {

    @Provides
    @ApplicationScope
    fun provideUserIdManager(impl: UserIdManagerImpl): UserIdManager = impl

    @Provides
    @ApplicationScope
    fun provideUserRepository(impl: UserRepositoryImpl): UserRepository = impl

    @Provides
    @ApplicationScope
    fun provideUserIdRemoteSource(impl: UserIdRemoteSourceImpl): UserIdRemoteSource = impl

    @Provides
    @ApplicationScope
    fun provideUserInfoRemoteSource(impl: UserInfoRemoteSourceImpl): UserInfoRemoteSource = impl
}
