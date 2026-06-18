plugins { id("katana.multiplatform.ui") }

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.common.media.data)
            api(projects.common.media.domain)
            api(projects.common.session.data)
            api(projects.common.session.domain)
            api(projects.common.user.data)
            api(projects.common.user.domain)

            api(projects.core.common)
            api(projects.core.domain)
            api(projects.core.preferences)
            api(projects.core.remote)
            api(projects.core.ui)

            api(projects.features.account.data)
            api(projects.features.account.domain)
            api(projects.features.account.ui)

            api(projects.features.explore.data)
            api(projects.features.explore.domain)
            api(projects.features.explore.ui)

            api(projects.features.home.data)
            api(projects.features.home.domain)
            api(projects.features.home.ui)

            api(projects.features.lists.data)
            api(projects.features.lists.domain)
            api(projects.features.lists.ui)
        }

        commonTest.dependencies {
            implementation(projects.core.tests)
            implementation(projects.core.tests.ui)
        }
    }
}
