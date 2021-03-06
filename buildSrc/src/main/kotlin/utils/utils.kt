package utils

import java.io.File
import org.sonarqube.gradle.SonarQubeProperties

fun SonarQubeProperties.addFilesIfExist(property: String, vararg paths: String) {
    paths.filter { file ->
        File(file).exists()
    }.takeUnless { files ->
        files.isEmpty()
    }?.joinToString(separator = ",")?.let { files ->
        property(property, files)
    }
}
