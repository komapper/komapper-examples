pluginManagement {
    val komapperVersion: String by settings
    val kspVersion: String by settings
    repositories {
        gradlePluginPortal()
        google()
    }
    plugins {
        id("org.komapper.gradle") version komapperVersion
        id("com.google.devtools.ksp") version kspVersion
    }
}

rootProject.name = "komapper-examples"
include("console")
include("spring-boot")
