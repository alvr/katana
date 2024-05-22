plugins {
    id("katana.multiplatform.core")
}

kotlin {
    sourceSets {
        commonMain.dependencies { implementation(projects.core.common) }
        commonTest.dependencies { implementation(projects.core.tests) }
    }
}
