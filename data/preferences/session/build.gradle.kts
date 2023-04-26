plugins {
    id("katana.multiplatform.data.preferences")
    alias(libs.plugins.serialization)
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.common.core)
        implementation(projects.data.preferences.base)
        implementation(projects.domain.session)
    }

    androidTestDependencies {
        implementation(projects.common.testsAndroid)
    }
}
