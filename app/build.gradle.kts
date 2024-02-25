plugins {
    id("katana.app.android")
}

dependencies {
    implementation(projects.data.preferences.base)
    implementation(projects.data.preferences.session)

    implementation(projects.data.remote.account)
    implementation(projects.data.remote.base)
    implementation(projects.data.remote.explore)
    implementation(projects.data.remote.lists)
    implementation(projects.data.remote.social)
    implementation(projects.data.remote.user)

    implementation(projects.domain.account)
    implementation(projects.domain.base)
    implementation(projects.domain.explore)
    implementation(projects.domain.lists)
    implementation(projects.domain.session)
    implementation(projects.domain.social)
    implementation(projects.domain.user)

    implementation(projects.ui.account)
    implementation(projects.ui.base)
    implementation(projects.ui.explore)
    implementation(projects.ui.lists)
    implementation(projects.ui.login)
    implementation(projects.ui.social)

    implementation(libs.compose.material3.windowsize)
    implementation(libs.sentry.multiplatform)
    implementation(libs.sentry.compose)

    testImplementation(projects.common.tests)

    // Kover
    kover(projects.data.preferences.base)
    kover(projects.data.preferences.session)

    kover(projects.data.remote.account)
    kover(projects.data.remote.base)
    kover(projects.data.remote.explore)
    kover(projects.data.remote.lists)
    kover(projects.data.remote.social)
    kover(projects.data.remote.user)

    kover(projects.ui.account)
    kover(projects.ui.base)
    kover(projects.ui.explore)
    kover(projects.ui.lists)
    kover(projects.ui.login)
    kover(projects.ui.social)
}
