package dev.alvr.katana.common.user.domain.di

import dev.alvr.katana.common.user.domain.repositories.UserRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockkClass
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
internal class CommonUserDomainModuleTest : FreeSpec({
    beforeSpec {
        MockProvider.register { clazz -> mockkClass(clazz) }
    }

    "verify commonUserDomainModule" - {
        commonUserDomainModule.verify(
            extraTypes = listOf(
                KatanaDispatcher::class,
                UserRepository::class,
            ),
        )
    }
})
