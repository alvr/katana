package dev.alvr.katana

import android.app.Application
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import dev.alvr.katana.common.tests.TestBase
import dev.alvr.katana.ui.main.di.katanaModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

@KoinExperimentalAPI
@ExperimentalCoroutinesApi
internal class KoinModulesTest : TestBase() {
    @Test
    fun `check koin modules`() = runTest {
        katanaModule.verify(
            extraTypes = listOf(
                Application::class,
                Context::class,
                SavedStateHandle::class,
            ),
        )
    }
}
