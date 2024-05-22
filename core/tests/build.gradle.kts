plugins {
    id("katana.multiplatform.tests")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)

            implementation(libs.arrow)
            implementation(libs.koin)
            implementation(libs.koin.test.get().toString()) { exclude(group = "junit", module = "junit") }
        }
    }
}
