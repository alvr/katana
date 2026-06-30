plugins {
    id("katana.multiplatform.data.preferences")
    id("katana.multiplatform.data.remote")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.common.media.data)
            implementation(projects.common.media.domain)
            implementation(projects.common.user.domain)

            implementation(projects.core.common)
            implementation(projects.core.preferences)
            implementation(projects.core.remote)

            implementation(projects.features.home.domain)
        }

        commonTest.dependencies { implementation(projects.core.tests) }
    }
}

katanaApollo.configure {
    dependsOn(projects.common.media.data)
}
