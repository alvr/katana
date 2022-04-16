package dev.alvr.katana.navigation.screens

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldNotBeInstanceOf

class ScreenTest : FeatureSpec({
    feature("parent screens") {
        scenario("without arguments") {
            with(Screen.Login) {
                route shouldBe "login"
                arguments.shouldBeEmpty()
                shouldNotBeInstanceOf<Screen.ChildScreen>()
            }

            with(Screen.Home) {
                route shouldBe "home"
                arguments.shouldBeEmpty()
                shouldNotBeInstanceOf<Screen.ChildScreen>()
            }
        }
    }

    feature("child screens") {
        scenario("without arguments") {
            with(Screen.Home.Lists) {
                route shouldBe "home/lists"
                arguments.shouldBeEmpty()
                shouldBeInstanceOf<Screen.ChildScreen>()
            }

            with(Screen.Home.Explore) {
                route shouldBe "home/explore"
                arguments.shouldBeEmpty()
                shouldBeInstanceOf<Screen.ChildScreen>()
            }

            with(Screen.Home.Social) {
                route shouldBe "home/social"
                arguments.shouldBeEmpty()
                shouldBeInstanceOf<Screen.ChildScreen>()
            }

            with(Screen.Home.Account) {
                route shouldBe "home/account"
                arguments.shouldBeEmpty()
                shouldBeInstanceOf<Screen.ChildScreen>()
            }
        }
    }
},)
