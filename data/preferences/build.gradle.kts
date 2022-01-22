import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    id("modules.android-library")
    alias(libs.plugins.protobuf)
}

protobuf {
    protoc {
        artifact = "${libs.protobuf.protoc.get()}"
    }

    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("java").option("lite")
            }
        }
    }
}

dependencies {
    implementation(libs.bundles.common.android)
    implementation(libs.bundles.data.preferences)

    testImplementation(libs.bundles.test)

    androidTestImplementation(libs.bundles.test.android)
}
