plugins {
    id("katana.test")
}

dependencies {
    implementation(libs.arrow)
    implementation(libs.koin)
    implementation(libs.koin.test) { exclude(group = "junit", module = "junit") }
    implementation(libs.mockmp)
}
