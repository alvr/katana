plugins {
    id("katana.multiplatform.core")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            api(projects.core.domain)
        }

        commonTest.dependencies { implementation(projects.core.tests) }
    }
}
