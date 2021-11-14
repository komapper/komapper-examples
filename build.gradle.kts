plugins {
    java
    kotlin("jvm")
    id("com.diffplug.spotless") version "6.0.0"
}

val ktlintVersion: String by project

val springBootProjects = subprojects.filter {
    it.name.startsWith("spring-boot")
}

allprojects {
    apply(plugin = "com.diffplug.spotless")

    repositories {
        mavenCentral()
    }

    spotless {
        kotlinGradle {
            ktlint(ktlintVersion)
        }
    }

    tasks {
        build {
            dependsOn(spotlessApply)
        }
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        if (project !in springBootProjects) {
            testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
            testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
        }
    }

    if (project.name != "codegen") {
        spotless {
            kotlin {
                targetExclude("build/**")
                ktlint(ktlintVersion)
            }
        }
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}
