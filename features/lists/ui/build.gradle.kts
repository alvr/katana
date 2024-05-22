plugins {
    id("katana.multiplatform.ui")
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
        }

        commonTest.dependencies {
            implementation(projects.core.tests)
        }
    }
}
