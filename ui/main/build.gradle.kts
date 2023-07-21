plugins {
    id("katana.multiplatform.compose")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(libs.compose.material3.windowsize)

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
    }

    androidMainDependencies {
        implementation(libs.sentry.compose)
    }

    commonTestDependencies {
        implementation(projects.common.tests)
    }
}
