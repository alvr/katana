plugins {
    id("katana.multiplatform.ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.common.session.domain)

            implementation(projects.core.common)
            implementation(projects.core.ui)

            implementation(projects.features.account.ui)
            implementation(projects.features.explore.ui)
            implementation(projects.features.lists.ui)
            implementation(projects.features.social.ui)
        }

        commonTest.dependencies { implementation(projects.core.tests) }
    }
}
