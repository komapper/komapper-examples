import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    idea
    id("com.google.devtools.ksp")
    id("org.springframework.boot") version "2.6.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.springframework.experimental.aot") version "0.11.2"
    kotlin("plugin.serialization")
    kotlin("plugin.spring")
}

val komapperVersion: String by project

dependencies {
    implementation("org.komapper:komapper-spring-boot-starter-r2dbc:$komapperVersion")
    implementation("org.komapper:komapper-spring-native-r2dbc:$komapperVersion")
    implementation("org.komapper:komapper-dialect-h2-r2dbc:$komapperVersion")
    ksp("org.komapper:komapper-processor:$komapperVersion")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

idea {
    module {
        sourceDirs = sourceDirs + file("build/generated/ksp/main/kotlin")
        testSourceDirs = testSourceDirs + file("build/generated/ksp/test/kotlin")
        generatedSourceDirs = generatedSourceDirs + file("build/generated/ksp/main/kotlin") + file("build/generated/ksp/test/kotlin")
    }
}

springAot {
    removeSpelSupport.set(true)
    removeYamlSupport.set(true)
}

springBoot {
    mainClass.set("org.komapper.example.ApplicationKt")
}

tasks.getByName<BootBuildImage>("bootBuildImage") {
    builder = "paketobuildpacks/builder:tiny"
    environment = mapOf(
        "BP_NATIVE_IMAGE" to "true"
    )
}
