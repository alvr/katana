import com.gradle.enterprise.gradleplugin.testretry.retry

plugins {
    id("katana.common")
    id("katana.dependency-versions")
    id("katana.detekt")
    id("katana.kover")
    id("katana.sonarqube")
}

tasks.withType<Test>().configureEach {
    retry {
        maxRetries.set(3)
        failOnPassedAfterRetry.set(false)
    }
}
