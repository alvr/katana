package dev.alvr.katana.data.preferences.token.managers

import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.token.managers.GetTokenBearerManager
import dev.alvr.katana.domain.token.usecases.GetAnilistTokenUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GetTokenBearerManagerImpl @Inject constructor(
    private val getAnilistTokenUseCase: GetAnilistTokenUseCase,
) : GetTokenBearerManager {
    override val token get() = getAnilistTokenUseCase.sync().orNull()?.token
}
