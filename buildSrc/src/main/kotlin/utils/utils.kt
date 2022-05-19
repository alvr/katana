package utils

import java.io.File
import org.sonarqube.gradle.SonarQubeProperties

fun SonarQubeProperties.addIfExists(property: String, path: String) {
    path.takeIf { file -> File(file).exists() }?.let {
        property(property, listOf(path))
    }
}
