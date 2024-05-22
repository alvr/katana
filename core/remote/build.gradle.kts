plugins {
    id("katana.multiplatform.data.remote")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.common.session.domain)
        }

        androidMain.dependencies {
            implementation(libs.sentry.apollo)
        }

        commonTest.dependencies {
            implementation(projects.core.tests)
        }
    }
}
