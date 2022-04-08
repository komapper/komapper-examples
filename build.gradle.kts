plugins {
    java
    kotlin("jvm")
    id("com.diffplug.spotless")version "6.4.2"
}

val springBootProjects = subprojects.filter {
    it.name.startsWith("spring-boot") || it.name == "jpetstore"
}

allprojects {
    apply(plugin = "base")
    apply(plugin = "com.diffplug.spotless")

    spotless {
        kotlin {
            ktlint()
            targetExclude("build/**")
            if (project.name == "codegen") {
                targetExclude("src/**")
            }
        }
        kotlinGradle {
            ktlint()
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://repo.spring.io/release") }
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
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

    kotlin {
        jvmToolchain {
            (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
        }
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}
