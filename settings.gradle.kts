pluginManagement {
    val kotlinVersion: String by settings
    val komapperVersion: String by settings
    val kspVersion: String by settings
    plugins {
        id("org.jetbrains.kotlin.jvm") version kotlinVersion
        id("org.jetbrains.kotlin.plugin.allopen") version kotlinVersion
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
