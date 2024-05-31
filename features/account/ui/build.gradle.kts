plugins {
    id("katana.multiplatform.ui")
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

        commonTest.dependencies { implementation(projects.core.tests) }
    }
}
