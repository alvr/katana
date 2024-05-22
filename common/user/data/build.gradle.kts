plugins {
    id("katana.multiplatform.data.remote")
}

dependencies {
    apolloMetadata(projects.core.remote)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.remote)
            implementation(projects.common.user.domain)
        }

        commonTest.dependencies {
            implementation(projects.core.tests)
        }
    }
}
