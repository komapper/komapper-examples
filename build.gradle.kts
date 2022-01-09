plugins {
    java
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
}

val springBootProjects = subprojects.filter {
    it.name.startsWith("spring-boot") || it.name == "jpetstore"
}

allprojects {
    apply(plugin = "base")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    ktlint {
        filter {
            exclude("build/**")
            if (project.name == "codegen") {
                exclude("src/**")
            }
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
        val pairs = listOf(
            "ktlintKotlinScriptCheck" to "ktlintKotlinScriptFormat",
            "ktlintMainSourceSetCheck" to "ktlintMainSourceSetFormat",
            "ktlintTestSourceSetCheck" to "ktlintTestSourceSetFormat",
        )
        for ((checkTask, formatTask) in pairs) {
            findByName(checkTask)?.mustRunAfter(formatTask)
        }

        build {
            dependsOn("ktlintFormat")
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
