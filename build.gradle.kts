plugins {
    kotlin("jvm")
    id("com.diffplug.spotless") version "5.15.2"
}

val ktlintVersion = "0.41.0"

val springBootProjects = subprojects.filter {
    it.name.startsWith("spring-boot")
}

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
        if (project !in springBootProjects) {
            "testImplementation"("org.junit.jupiter:junit-jupiter-api:5.8.1")
            "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.8.1")
        }
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
