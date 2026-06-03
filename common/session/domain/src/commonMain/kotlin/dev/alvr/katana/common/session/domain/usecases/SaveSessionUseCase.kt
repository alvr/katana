package dev.alvr.katana.common.session.domain.usecases

import dev.alvr.katana.common.session.domain.models.AnilistToken
import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.core.domain.usecases.EitherUseCase
import dev.alvr.katana.core.domain.usecases.KatanaEitherUseCase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.binding

interface SaveSessionUseCase : KatanaEitherUseCase<AnilistToken, Unit>

@ContributesBinding(AppScope::class, binding = binding<SaveSessionUseCase>())
internal class SaveSessionUseCaseImpl(dispatcher: KatanaDispatcher, private val repository: SessionRepository) :
    EitherUseCase<AnilistToken, Unit>(dispatcher), SaveSessionUseCase {
    override suspend fun run(params: AnilistToken) = repository.saveSession(params)
}
