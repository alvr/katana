plugins {
    id("katana.feature.data.remote")
}

dependencies {
    implementation(projects.common.core)
    implementation(projects.domain.session)

    implementation(libs.sentry.apollo)

    testImplementation(projects.common.tests)
}
