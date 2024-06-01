plugins {
    id("katana.multiplatform.data.preferences")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.domain)
        }

        commonTest.dependencies { implementation(projects.core.tests) }
    }
}
