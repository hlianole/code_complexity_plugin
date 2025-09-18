rootProject.name = "Java Code Complexity Analyzer"

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven("https://cache-redirector.jetbrains.com/plugins.gradle.org")
    }

    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    }
}
