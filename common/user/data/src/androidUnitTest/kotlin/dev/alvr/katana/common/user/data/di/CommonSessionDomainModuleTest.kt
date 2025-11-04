package dev.alvr.katana.common.user.data.di

import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockkClass
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.mock.MockProvider

@OptIn(KoinExperimentalAPI::class)
internal class CommonSessionDomainModuleTest : FreeSpec({
    beforeSpec {
        MockProvider.register { clazz -> mockkClass(clazz) }
    }

    "verify commonUserDataModule" - {
//        commonUserDataModule.verify()
    }
})
