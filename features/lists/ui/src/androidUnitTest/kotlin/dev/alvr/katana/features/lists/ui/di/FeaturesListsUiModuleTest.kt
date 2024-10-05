package dev.alvr.katana.features.lists.ui.di

import dev.alvr.katana.features.lists.domain.usecases.ObserveAnimeListUseCase
import dev.alvr.katana.features.lists.domain.usecases.ObserveMangaListUseCase
import dev.alvr.katana.features.lists.domain.usecases.UpdateListUseCase
import io.kotest.core.spec.style.FreeSpec
import io.mockk.mockkClass
import org.koin.test.mock.MockProvider
import org.koin.test.verify.verify

internal class FeaturesListsUiModuleTest : FreeSpec({
    beforeSpec {
        MockProvider.register { clazz -> mockkClass(clazz) }
    }

    "verify featuresListsUiModule" - {
        featuresListsUiModule.verify(
            extraTypes = listOf(
                ObserveAnimeListUseCase::class,
                ObserveMangaListUseCase::class,
                UpdateListUseCase::class,
            ),
        )
    }
})
