pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
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
include("quarkus-jdbc")
