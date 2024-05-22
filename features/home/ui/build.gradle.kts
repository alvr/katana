plugins {
    id("katana.multiplatform.ui")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.ui)
        }

        commonTest.dependencies {
            implementation(projects.core.tests)
        }
    }
}
