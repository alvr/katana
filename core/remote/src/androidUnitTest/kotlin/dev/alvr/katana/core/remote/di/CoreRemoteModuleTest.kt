package dev.alvr.katana.core.remote.di

import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockkClass
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify

internal class CoreRemoteModuleTest : FreeSpec({
    beforeSpec {
        MockProvider.register { clazz -> mockkClass(clazz) }
    }

    "verify coreRemoteModule" - {
        coreRemoteModule.verify()
    }
})
