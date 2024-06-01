plugins {
    id("katana.multiplatform.data.preferences")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.preferences)
            implementation(projects.common.session.domain)
        }

        commonTest.dependencies { implementation(projects.core.tests) }
    }
}
