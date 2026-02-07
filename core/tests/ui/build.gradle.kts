plugins { id("katana.multiplatform.ui") }

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.ui)
            implementation(libs.bundles.ui.common)
            implementation(libs.bundles.core.common.test)
        }
    }
}
