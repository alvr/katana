plugins {
    id("katana.android.library")
}

dependencies {
    implementation(projects.common.core)
    implementation(projects.domain.base)

    implementation(libs.bundles.data.preferences)

    testImplementation(projects.common.testsAndroid)
    kaptTest(libs.bundles.kapt)
}
