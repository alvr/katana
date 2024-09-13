package dev.alvr.katana.core.preferences.di

import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockkClass
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify

internal class CorePreferencesModuleTest : FreeSpec({
    beforeSpec {
        MockProvider.register { clazz -> mockkClass(clazz) }
    }

    "verify corePreferencesModule" - {
        corePreferencesModule.verify()
    }
})
