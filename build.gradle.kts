import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.0"
    id("com.diffplug.spotless") version "5.12.5"
}

allprojects {
    apply(plugin = "com.diffplug.spotless")

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlinGradle {
            ktlint("0.41.0")
        }
    }

    tasks {
        build {
            dependsOn(spotlessApply)
        }
    }

    repositories {
        mavenCentral()
        google()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    val javaLanguageVersion: JavaLanguageVersion = JavaLanguageVersion.of(11)

    java {
        toolchain {
            languageVersion.set(javaLanguageVersion)
        }
    }

    tasks.withType<KotlinCompile> {
        val jvmTarget = javaLanguageVersion.toString()
        kotlinOptions.jvmTarget = jvmTarget
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    dependencies {
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:5.7.1")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.7.1")
    }

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            targetExclude("build/**")
            ktlint("0.41.0")
        }
    }
}
