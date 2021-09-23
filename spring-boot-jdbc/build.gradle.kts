plugins {
    id("org.springframework.boot") version "2.5.5"
    id("com.google.devtools.ksp")
    kotlin("plugin.allopen")
}

apply(plugin = "io.spring.dependency-management")

val komapperVersion: String by project

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.komapper:komapper-spring-boot-starter-jdbc:$komapperVersion")
    implementation("org.komapper:komapper-dialect-h2-jdbc:$komapperVersion")
    ksp("org.komapper:komapper-processor:$komapperVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

allOpen {
    annotation("org.springframework.context.annotation.Configuration")
    annotation("org.springframework.transaction.annotation.Transactional")
}

springBoot {
    mainClass.set("org.komapper.example.ApplicationKt")
}
