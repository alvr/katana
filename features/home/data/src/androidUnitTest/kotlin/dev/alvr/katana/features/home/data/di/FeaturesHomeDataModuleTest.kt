package dev.alvr.katana.features.home.data.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.interceptor.ApolloInterceptor
import dev.alvr.katana.common.user.domain.managers.UserIdManager
import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockkClass
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
internal class FeaturesHomeDataModuleTest : FreeSpec({
    beforeSpec {
        MockProvider.register { clazz -> mockkClass(clazz) }
    }

    "verify featuresHomeDataModule" - {
        featuresHomeDataModule.verify(
            extraTypes = listOf(
                ApolloClient::class,
                ApolloInterceptor::class,
                UserIdManager::class,
            ),
        )
    }
})
