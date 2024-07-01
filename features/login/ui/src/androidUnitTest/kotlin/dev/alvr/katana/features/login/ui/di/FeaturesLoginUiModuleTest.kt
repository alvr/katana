package dev.alvr.katana.features.login.ui.di

import dev.alvr.katana.common.session.domain.usecases.SaveSessionUseCase
import dev.alvr.katana.common.user.domain.usecases.SaveUserIdUseCase
import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockkClass
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
internal class FeaturesLoginUiModuleTest : FreeSpec({
    beforeSpec {
        MockProvider.register { clazz -> mockkClass(clazz) }
    }

    "verify featuresLoginUiModule" - {
        featuresLoginUiModule.verify(
            extraTypes = listOf(
                SaveSessionUseCase::class,
                SaveUserIdUseCase::class,
            ),
        )
    }
})
