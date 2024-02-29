plugins {
    id("katana.multiplatform.ui")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(libs.compose.material3.windowsize)
        implementation(libs.sentry.multiplatform)

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
        implementation(projects.core.tests)
    }
}

dependencies {
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
