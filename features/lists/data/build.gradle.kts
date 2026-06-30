plugins { id("katana.multiplatform.data.remote") }

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.common.media.data)
            implementation(projects.common.media.domain)
            implementation(projects.common.user.domain)

            implementation(projects.core.common)
            implementation(projects.core.remote)

            implementation(projects.features.lists.domain)
        }

        commonTest.dependencies { implementation(projects.core.tests) }
    }
}

katanaApollo.configure {
    dependsOn(projects.common.media.data)
}
