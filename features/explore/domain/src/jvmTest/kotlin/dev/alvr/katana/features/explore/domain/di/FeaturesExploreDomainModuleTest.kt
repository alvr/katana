package dev.alvr.katana.features.explore.domain.di

import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockkClass
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify

internal class FeaturesExploreDomainModuleTest : FreeSpec({
    beforeSpec {
        MockProvider.register { clazz -> mockkClass(clazz) }
    }

    "verify featuresExploreDomainModule" - {
        featuresExploreDomainModule.verify()
    }
})
