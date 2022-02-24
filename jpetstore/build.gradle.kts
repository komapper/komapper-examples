plugins {
    idea
    id("org.springframework.boot") version "2.6.3"
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
    implementation("org.komapper:komapper-sqlcommenter:$komapperVersion")
    implementation("org.komapper:komapper-dialect-h2-jdbc:$komapperVersion")
    ksp("org.komapper:komapper-processor:$komapperVersion")

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.h2database:h2:2.1.210")
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
