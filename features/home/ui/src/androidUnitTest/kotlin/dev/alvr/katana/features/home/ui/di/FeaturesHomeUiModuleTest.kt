package dev.alvr.katana.features.home.ui.di

import dev.alvr.katana.common.session.domain.usecases.ClearActiveSessionUseCase
import dev.alvr.katana.common.session.domain.usecases.ObserveActiveSessionUseCase
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
                ClearActiveSessionUseCase::class,
                ObserveActiveSessionUseCase::class,
            ),
        )
    }
})
