plugins {
    id("katana.multiplatform.data.remote")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.common.core)
        implementation(projects.domain.session)
    }

    androidMainDependencies {
        implementation(libs.sentry.apollo)
    }

    commonTestDependencies {
        implementation(projects.common.tests)
    }
}
