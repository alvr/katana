package dev.alvr.katana.common.tests

import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.KoinApplication
import org.koin.test.KoinTest
import org.koin.test.junit5.KoinTestExtension
import org.koin.test.junit5.mock.MockProviderExtension

@ExperimentalCoroutinesApi
abstract class KoinTest5 : TestBase(), KoinTest {
    @JvmField
    @RegisterExtension
    internal val koinTestExtension = KoinTestExtension.create { initKoin() }

    @JvmField
    @RegisterExtension
    internal val mockProvider = MockProviderExtension.create { clazz -> mockkClass(clazz) }

    protected abstract fun KoinApplication.initKoin()
}
