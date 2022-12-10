plugins {
    idea
    id("org.springframework.boot")
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
    platform("org.komapper:komapper-platform:$komapperVersion").let {
        implementation(it)
        ksp(it)
    }
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.komapper:komapper-spring-boot-starter-r2dbc")
    implementation("org.komapper:komapper-dialect-h2-r2dbc")
    ksp("org.komapper:komapper-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

springBoot {
    mainClass.set("org.komapper.example.ApplicationKt")
}
