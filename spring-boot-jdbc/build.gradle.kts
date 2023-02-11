plugins {
    idea
    id("org.springframework.boot")
    id("com.google.devtools.ksp")
    id("org.graalvm.buildtools.native")
    kotlin("plugin.spring")
}

apply(plugin = "io.spring.dependency-management")

val komapperVersion: String by project

dependencies {
    platform("org.komapper:komapper-platform:$komapperVersion").let {
        implementation(it)
        ksp(it)
    }
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.komapper:komapper-spring-boot-starter-jdbc")
    implementation("org.komapper:komapper-dialect-h2-jdbc")
    ksp("org.komapper:komapper-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

springBoot {
    mainClass.set("org.komapper.example.ApplicationKt")
}
