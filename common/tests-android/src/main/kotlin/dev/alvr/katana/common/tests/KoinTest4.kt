package dev.alvr.katana.common.tests

import android.app.Application
import android.content.Context
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.koin.core.KoinApplication
import org.koin.dsl.binds
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule

@ExperimentalCoroutinesApi
abstract class KoinTest4 : RoboTest(), KoinTest {
    @get:Rule
    internal val koinTestRule = KoinTestRule.create {
        koin.loadModules(
            listOf(
                module { single { context } binds arrayOf(Context::class, Application::class) },
            ),
        )
        initKoin()
    }

    @get:Rule
    internal val mockProvider = MockProviderRule.create { clazz -> mockkClass(clazz) }

    protected abstract fun KoinApplication.initKoin()
}
