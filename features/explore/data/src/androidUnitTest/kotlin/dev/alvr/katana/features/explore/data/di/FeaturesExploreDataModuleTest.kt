package dev.alvr.katana.features.explore.data.di

import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockkClass
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
internal class FeaturesExploreDataModuleTest : FreeSpec({
    beforeSpec {
        MockProvider.register { clazz -> mockkClass(clazz) }
    }

    "verify featuresExploreDataModule" - {
        featuresExploreDataModule.verify()
    }
})
