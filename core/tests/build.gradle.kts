plugins { id("katana.multiplatform.tests") }

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)

            implementation(libs.arrow)
        }
    }
}
