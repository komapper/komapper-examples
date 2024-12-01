plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.spotless)
}

val springBootProjects = subprojects.filter {
    it.name.startsWith("spring-boot") || it.name == "jpetstore"
}

// Retain a reference to rootProject.libs to make the version catalog accessible within allprojects and subprojects.
// See https://github.com/gradle/gradle/issues/16708
val catalog = libs

allprojects {
    apply(plugin = "base")
    apply(plugin = catalog.plugins.spotless.get().pluginId)

    spotless {
        kotlin {
            ktlint(catalog.ktlint.get().version)
            targetExclude("build/**")
            if (project.name == "codegen") {
                targetExclude("src/**")
            }
        }
        kotlinGradle {
            ktlint(catalog.ktlint.get().version)
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()
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
            testImplementation(catalog.junit.api)
            testRuntimeOnly(catalog.junit.engine)
        }
    }

    tasks {
        withType<Test>().configureEach {
            useJUnitPlatform()
        }

        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
            kotlinOptions.jvmTarget = "17"
        }
    }
}

// TODO Remove this workaround in the future
// https://github.com/quarkusio/quarkus/issues/29698#issuecomment-1671861607
project.afterEvaluate {
    getTasksByName("quarkusGenerateCode", true).forEach { task ->
        task.setDependsOn(task.dependsOn.filterIsInstance<Provider<Task>>().filter { it.get().name != "processResources" })
    }
    getTasksByName("quarkusGenerateCodeDev", true).forEach { task ->
        task.setDependsOn(task.dependsOn.filterIsInstance<Provider<Task>>().filter { it.get().name != "processResources" })
    }
}
