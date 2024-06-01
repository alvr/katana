plugins {
    id("katana.multiplatform.ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.ui)

            implementation(projects.common.session.domain)
            implementation(projects.common.user.domain)
        }

        commonTest.dependencies { implementation(projects.core.tests) }
    }
}
