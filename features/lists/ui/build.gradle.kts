plugins {
    id("katana.multiplatform.ui")
    id("kotlin-parcelize")
    alias(libs.plugins.parcelize)
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "lists")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.ui)

            implementation(projects.features.lists.domain)

            implementation(libs.parcelable)
        }

        commonTest.dependencies {
            implementation(projects.core.tests)
        }
    }
}
