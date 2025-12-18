package dev.alvr.katana.features.home.domain.di

import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.features.home.domain.repositories.HomeRepository
import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockkClass
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
internal class FeaturesHomeDomainModuleTest : FreeSpec({
    beforeSpec {
        MockProvider.register { clazz -> mockkClass(clazz) }
    }

    "verify featuresHomeDomainModule" - {
        featuresHomeDomainModule.verify(
            extraTypes = listOf(
                KatanaDispatcher::class,
                HomeRepository::class,
            ),
        )
    }
})
