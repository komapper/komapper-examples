plugins {
    idea
    id("org.springframework.boot") version "2.6.2"
    id("com.google.devtools.ksp")
    kotlin("plugin.spring")
}

apply(plugin = "io.spring.dependency-management")

val komapperVersion: String by project

idea {
    module {
        sourceDirs = sourceDirs + file("build/generated/ksp/main/kotlin")
        testSourceDirs = testSourceDirs + file("build/generated/ksp/test/kotlin")
        generatedSourceDirs = generatedSourceDirs + file("build/generated/ksp/main/kotlin") + file("build/generated/ksp/test/kotlin")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.komapper:komapper-spring-boot-starter-jdbc:$komapperVersion")
    implementation("org.komapper:komapper-sqlcommenter:$komapperVersion")
    implementation("org.komapper:komapper-dialect-h2-jdbc:$komapperVersion")
    ksp("org.komapper:komapper-processor:$komapperVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

springBoot {
    mainClass.set("org.komapper.example.ApplicationKt")
}
