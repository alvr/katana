plugins {
    id("katana.multiplatform.app")
}

kotlin {
    sourceSets {
        commonMain.dependencies { implementation(projects.shared) }
    }
}
