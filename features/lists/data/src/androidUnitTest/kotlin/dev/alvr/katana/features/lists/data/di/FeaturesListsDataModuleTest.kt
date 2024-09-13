package dev.alvr.katana.features.lists.data.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.interceptor.ApolloInterceptor
import dev.alvr.katana.common.user.domain.managers.UserIdManager
import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockkClass
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify

internal class FeaturesListsDataModuleTest : FreeSpec({
    beforeSpec {
        MockProvider.register { clazz -> mockkClass(clazz) }
    }

    "verify featuresListsDataModule" - {
        featuresListsDataModule.verify(
            extraTypes = listOf(
                ApolloClient::class,
                ApolloInterceptor::class,
                UserIdManager::class,
            ),
        )
    }
})
