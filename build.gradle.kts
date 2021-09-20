plugins {
    kotlin("jvm")
    id("com.diffplug.spotless") version "5.15.1"
}

val ktlintVersion = "0.41.0"

allprojects {
    apply(plugin = "com.diffplug.spotless")

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlinGradle {
            ktlint(ktlintVersion)
        }
    }

    tasks {
        build {
            dependsOn(spotlessApply)
        }
    }

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    dependencies {
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:5.7.2")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    }

    if (project.name != "codegen") {
        configure<com.diffplug.gradle.spotless.SpotlessExtension> {
            kotlin {
                targetExclude("build/**")
                ktlint(ktlintVersion)
            }
        }
    }
}
