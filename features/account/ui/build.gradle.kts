plugins {
    id("katana.multiplatform.ui")
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "account")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.common.session.domain)
            implementation(projects.common.user.domain)

            implementation(projects.core.common)
            implementation(projects.core.ui)

            implementation(projects.features.account.domain)
        }

        commonTest.dependencies {
            implementation(projects.core.tests)
        }
    }
}
