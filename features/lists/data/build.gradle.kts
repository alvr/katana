plugins {
    id("katana.multiplatform.data.remote")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.common.user.domain)

            implementation(projects.core.common)
            implementation(projects.core.remote)

            implementation(projects.features.lists.domain)
        }

        commonTest.dependencies {
            implementation(projects.core.tests)
        }
    }
}
