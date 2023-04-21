plugins {
    id("katana.multiplatform.data.remote")
}

android.buildFeatures.buildConfig = true

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.common.core)
        implementation(projects.domain.session)
        implementation(libs.apollo.cache.sql)
    }

    androidMainDependencies {
        implementation(libs.okhttp)
        implementation(libs.okhttp.logger)
        implementation(libs.sentry.apollo)
    }

    commonTestDependencies {
        implementation(projects.common.tests)
    }
}
