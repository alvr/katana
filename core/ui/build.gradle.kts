plugins {
    id("katana.multiplatform.ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(libs.compose.placeholder)
            implementation(libs.compose.windowsize)
            implementation(libs.materialkolor)
        }

        commonTest.dependencies {
            implementation(projects.core.tests)
        }
    }
}
