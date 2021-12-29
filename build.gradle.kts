plugins {
    java
    kotlin("jvm")
    id("com.diffplug.spotless") version "6.1.0"
}

val ktlintVersion: String by project

val springBootProjects = subprojects.filter {
    it.name.startsWith("spring-boot") || it.name == "jpetstore"
}

allprojects {
    apply(plugin = "base")
    apply(plugin = "com.diffplug.spotless")

    repositories {
        mavenCentral()
        mavenLocal()
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
        testImplementation(kotlin("test"))
        if (project !in springBootProjects) {
            testImplementation(kotlin("test"))
            testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
            testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
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
