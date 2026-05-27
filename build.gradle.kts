plugins {
    id("org.jetbrains.intellij.platform") version "2.16.0"
    kotlin("jvm") version "2.0.21"
}

group = "com.dc7c5"
version = "1.0.0"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    implementation(kotlin("stdlib"))

    intellijPlatform {
        create("IC", "2025.1")
        bundledPlugins("com.intellij.java", "org.jetbrains.kotlin")
    }
}

// Require Java 17+ (required by modern IntelliJ Platform Gradle Plugin)
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

intellijPlatform {
    pluginConfiguration {
        name.set("Escape Viewer")
    }
}

tasks {
    patchPluginXml {
        sinceBuild.set("242")
        untilBuild.set("999.*")   // Support all future IDE builds (PyCharm 2025.1+, IDEA 2025.x, etc.)
        pluginDescription.set("Toggle between rendered characters and raw escape sequences (<AXX> / <UXXXX>) in string literals.")
        changeNotes.set("Initial production release with Inlay Hints support.")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
