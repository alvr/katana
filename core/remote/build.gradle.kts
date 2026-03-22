plugins { id("katana.multiplatform.data.remote") }

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.atomicfu)
            implementation(projects.core.common)
            implementation(projects.common.session.domain)
        }

        commonTest.dependencies { implementation(projects.core.tests) }
    }
}
