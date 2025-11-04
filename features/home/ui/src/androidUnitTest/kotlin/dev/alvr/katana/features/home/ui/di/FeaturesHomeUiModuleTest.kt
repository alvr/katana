package dev.alvr.katana.features.home.ui.di

import androidx.lifecycle.SavedStateHandle
import dev.alvr.katana.common.session.domain.usecases.ObserveActiveSessionUseCase
import dev.alvr.katana.common.session.domain.usecases.SaveSessionUseCase
import dev.alvr.katana.common.user.domain.usecases.SaveUserIdUseCase
import dev.alvr.katana.features.home.domain.usecases.HideWelcomeCardUseCase
import dev.alvr.katana.features.home.domain.usecases.ObserveWelcomeCardVisibilityUseCase
import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockkClass
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
internal class FeaturesHomeUiModuleTest : FreeSpec({
    beforeSpec {
        MockProvider.register { clazz -> mockkClass(clazz) }
    }

    "verify featuresHomeUiModule" - {
        featuresHomeUiModule.verify(
            extraTypes = listOf(
                SavedStateHandle::class,
                HideWelcomeCardUseCase::class,
                ObserveActiveSessionUseCase::class,
                ObserveWelcomeCardVisibilityUseCase::class,
                SaveSessionUseCase::class,
                SaveUserIdUseCase::class,
            ),
        )
    }
})
