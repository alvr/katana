plugins { id("katana.multiplatform.core") }

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.common.media.domain)

            implementation(projects.core.common)
            api(projects.core.domain)
        }

        commonTest.dependencies { implementation(projects.core.tests) }
    }
}
