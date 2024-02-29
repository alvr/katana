plugins {
    id("katana.multiplatform.app")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(libs.compose.material3.windowsize)
        implementation(libs.sentry.multiplatform)

        implementation(projects.data.preferences.base)
        implementation(projects.data.preferences.session)

        implementation(projects.data.remote.base)
        implementation(projects.data.remote.user)

        implementation(projects.domain.base)
        implementation(projects.domain.session)
        implementation(projects.domain.user)

        implementation(projects.ui.base)

        implementation(projects.features.account.data)
        implementation(projects.features.account.domain)
        implementation(projects.features.account.ui)

        implementation(projects.features.explore.data)
        implementation(projects.features.explore.domain)
        implementation(projects.features.explore.ui)

        implementation(projects.features.lists.data)
        implementation(projects.features.lists.domain)
        implementation(projects.features.lists.ui)

        implementation(projects.features.login.ui)

        implementation(projects.features.social.data)
        implementation(projects.features.social.domain)
        implementation(projects.features.social.ui)
    }

    androidMainDependencies {
        implementation(libs.sentry.compose)
    }

    commonTestDependencies {
        implementation(projects.common.tests)
    }
}

dependencies {
    kover(projects.data.preferences.base)
    kover(projects.data.preferences.session)

    kover(projects.data.remote.base)
    kover(projects.data.remote.user)

    kover(projects.ui.base)

    kover(projects.features.account.data)
    kover(projects.features.account.ui)

    kover(projects.features.explore.data)
    kover(projects.features.explore.ui)

    kover(projects.features.lists.data)
    kover(projects.features.lists.ui)

    kover(projects.features.login.ui)

    kover(projects.features.social.data)
    kover(projects.features.social.ui)
}
