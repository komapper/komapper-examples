pluginManagement {
    val kspVersion: String by settings
    repositories {
        gradlePluginPortal()
        google()
    }
    plugins {
        id("com.google.devtools.ksp") version kspVersion
    }
}

rootProject.name = "komapper-examples"
include("console")
include("spring-boot")
