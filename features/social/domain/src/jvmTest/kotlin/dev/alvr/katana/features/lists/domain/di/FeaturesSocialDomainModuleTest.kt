package dev.alvr.katana.features.lists.domain.di

import dev.alvr.katana.features.social.domain.di.featuresSocialDomainModule
import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockkClass
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify

internal class FeaturesSocialDomainModuleTest : FreeSpec({
    beforeSpec {
        MockProvider.register { clazz -> mockkClass(clazz) }
    }

    "verify featuresSocialDomainModule" - {
        featuresSocialDomainModule.verify()
    }
})
