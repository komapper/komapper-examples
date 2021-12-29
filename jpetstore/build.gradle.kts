plugins {
    id("org.springframework.boot") version "2.6.2"
    id("com.google.devtools.ksp")
    kotlin("plugin.spring")
}

apply(plugin = "io.spring.dependency-management")

// val komapperVersion: String by project
val komapperVersion = "0.24.1-SNAPSHOT"

kotlin {
    sourceSets.main {
        kotlin.srcDirs("build/generated/ksp/main/kotlin")
    }
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
    }
}

springBoot {
    mainClass.set("org.komapper.example.ApplicationKt")
}

repositories {
    mavenCentral()
    mavenLocal()
    maven(url = "https://repo.spring.io/milestone")
}

dependencies {
    implementation("org.komapper:komapper-spring-boot-starter-jdbc:$komapperVersion")
    implementation("org.komapper:komapper-dialect-h2-jdbc:$komapperVersion")
    ksp("org.komapper:komapper-processor:$komapperVersion")

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.h2database:h2:1.4.200")
    implementation("org.webjars:jquery:3.6.0")
    implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.0.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}
