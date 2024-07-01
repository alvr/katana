package dev.alvr.katana.features.account.ui.di

import dev.alvr.katana.common.session.domain.usecases.LogOutUseCase
import dev.alvr.katana.common.user.domain.usecases.ObserveUserInfoUseCase
import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockkClass
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
internal class FeaturesAccountUiModuleTest : FreeSpec({
    beforeSpec {
        MockProvider.register { clazz -> mockkClass(clazz) }
    }

    "verify featuresAccountUiModule" - {
        featuresAccountUiModule.verify(
            extraTypes = listOf(
                LogOutUseCase::class,
                ObserveUserInfoUseCase::class,
            ),
        )
    }
})
