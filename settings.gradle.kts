pluginManagement {
    repositories {
        mavenLocal()
        maven { url = uri("https://repo.spring.io/release") }
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
        gradlePluginPortal()
        mavenCentral()
    }
    val kotlinVersion: String by settings
    val komapperVersion: String by settings
    val kspVersion: String by settings
    plugins {
        id("org.jetbrains.kotlin.jvm") version kotlinVersion
        id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
        id("org.jetbrains.kotlin.plugin.serialization") version kotlinVersion
        id("org.komapper.gradle") version komapperVersion
        id("com.google.devtools.ksp") version kspVersion
    }
}

rootProject.name = "komapper-examples"
include("codegen")
include("console-jdbc")
include("console-r2dbc")
include("spring-boot-jdbc")
include("spring-boot-r2dbc")
include("comparison-with-exposed")
include("repository-pattern-jdbc")
include("jpetstore")
include("kweet")
include("spring-native-jdbc")
include("spring-native-r2dbc")
