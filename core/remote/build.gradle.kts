plugins {
    id("katana.multiplatform.data.remote")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.core.common)
        implementation(projects.common.session.domain)
    }

    androidMainDependencies {
        implementation(libs.sentry.apollo)
    }

    commonTestDependencies {
        implementation(projects.core.tests)
    }
}
