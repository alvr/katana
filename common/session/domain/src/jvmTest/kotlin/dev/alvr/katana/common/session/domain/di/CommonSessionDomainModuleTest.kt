package dev.alvr.katana.common.session.domain.di

import dev.alvr.katana.common.session.domain.repositories.SessionRepository
import dev.alvr.katana.core.common.coroutines.KatanaDispatcher
import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockkClass
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
internal class CommonSessionDomainModuleTest : FreeSpec({
    beforeSpec {
        MockProvider.register { clazz -> mockkClass(clazz) }
    }

    "verify commonSessionDomainModule" - {
        commonSessionDomainModule.verify(
            extraTypes = listOf(
                KatanaDispatcher::class,
                SessionRepository::class,
            ),
        )
    }
})
