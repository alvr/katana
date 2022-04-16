package dev.alvr.katana

import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import com.appmattus.kotlinfixture.kotlinFixture
import dev.alvr.katana.domain.base.usecases.sync
import dev.alvr.katana.domain.token.usecases.GetAnilistTokenUseCase
import dev.alvr.katana.navigation.screens.Screen
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

internal class MainViewModelTest : BehaviorSpec({
    given("the MainViewModel") {
        val fixture = kotlinFixture { nullabilityStrategy(NeverNullStrategy) }

        val getAnilistTokenUseCase = mockk<GetAnilistTokenUseCase>()
        val vm = MainViewModel(getAnilistTokenUseCase)

        `when`("the user does not have a saved token") {
            coEvery { getAnilistTokenUseCase.sync() } returns null

            then("the initial route should be login") {
                vm.initialRoute shouldBe Screen.Login.route
            }
        }

        `when`("the user does have a saved token") {
            coEvery { getAnilistTokenUseCase.sync() } returns fixture()

            then("the initial route should be home") {
                vm.initialRoute shouldBe Screen.Home.route
            }
        }
    }
},)
