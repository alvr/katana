plugins { id("katana.multiplatform.data.remote") }

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.common.media.domain)
            implementation(projects.common.user.domain)

            implementation(projects.core.common)
            implementation(projects.core.remote)
        }

        commonTest.dependencies { implementation(projects.core.tests) }
    }
}
