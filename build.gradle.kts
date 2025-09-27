import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java")
    alias(libs.plugins.intelliJPlatform)
    alias(libs.plugins.changelog)
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    intellijPlatform {
        create(
            providers.gradleProperty("platformType"),
            providers.gradleProperty("platformVersion")
        )

        bundledPlugin("com.intellij.java")

        testFramework(TestFrameworkType.Platform)
    }

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.5.0")
}

changelog {
    groups.empty()
    repositoryUrl = providers.gradleProperty("pluginRepositoryUrl")
    version = providers.gradleProperty("pluginVersion")
}

intellijPlatform {
    pluginConfiguration {
        name = providers.gradleProperty("pluginName")
        version = providers.gradleProperty("pluginVersion")

        description = providers.gradleProperty("pluginDescription").orElse(
            """
            Code Complexity Analyzer for Java applications.
            
            Features:
            • Cyclomatic complexity analysis
            • Maintainability index calculation  
            • Real-time code quality metrics
            • Visual indicators in editor
            """
        )

        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
            untilBuild = providers.gradleProperty("pluginUntilBuild")
        }

        changeNotes = provider {
            changelog.renderItem(
                changelog.getOrNull(providers.gradleProperty("pluginVersion").get())
                    ?: changelog.getUnreleased()
            )
        }
    }

    pluginVerification {
        ides {
            recommended()
        }
        freeArgs = listOf(
            "-mute",
            "TemplateWordInPluginId",
            "ForbiddenPluginIdPrefix",
            "PluginNeverLoaded",
            "MissingDependency",
            "InvalidPlugin",
            "NotFound"
        )
    }

    signing {
        certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("PRIVATE_KEY")
        password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    }

    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
    }
}

tasks {
    wrapper {
        gradleVersion = providers.gradleProperty("gradleVersion").get()
    }

    test {
        //useJUnitPlatform()
    }

    patchPluginXml {
        changeNotes = provider {
            changelog.renderItem(
                changelog.getOrNull(providers.gradleProperty("pluginVersion").get())
                    ?: changelog.getUnreleased()
            )
        }
    }
}
