package dev.alvr.katana.features.lists.domain.di

import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import dev.alvr.katana.features.lists.domain.repositories.ListsRepository
import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockkClass
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
internal class FeaturesListsDomainModuleTest : FreeSpec({
    beforeSpec {
        MockProvider.register { clazz -> mockkClass(clazz) }
    }

    "verify featuresListsDomainModule" - {
        featuresListsDomainModule.verify(
            extraTypes = listOf(
                KatanaDispatcher::class,
                ListsRepository::class,
            ),
        )
    }
})
