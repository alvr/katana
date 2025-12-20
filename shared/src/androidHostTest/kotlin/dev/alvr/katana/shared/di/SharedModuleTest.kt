package dev.alvr.katana.shared.di

import androidx.lifecycle.SavedStateHandle
import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockkClass
import okio.Path
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
internal class SharedModuleTest : FreeSpec({
    beforeSpec {
        MockProvider.register { clazz -> mockkClass(clazz) }
    }

    "verify katanaModule" - {
        katanaModule.verify(
            extraTypes = listOf(
                Path::class,
                SavedStateHandle::class,
            ),
        )
    }
})
