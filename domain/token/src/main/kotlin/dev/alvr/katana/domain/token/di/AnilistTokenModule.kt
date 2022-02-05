package dev.alvr.katana.domain.token.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.alvr.katana.domain.base.sync
import dev.alvr.katana.domain.token.usecases.GetAnilistTokenUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AnilistTokenModule {

    @Provides
    @Singleton
    @AnilistToken
    fun provideAnilistToken(getAnilistTokenUseCase: GetAnilistTokenUseCase): String? =
        getAnilistTokenUseCase.sync()?.token
}
