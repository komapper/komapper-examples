plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.dependency.management)
}

springBoot {
    mainClass.set("org.komapper.example.ApplicationKt")
}

dependencyManagement {
    dependencies {
        dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core:${libs.versions.coroutines.get()}")
        dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${libs.versions.coroutines.get()}")
        dependency("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:${libs.versions.coroutines.get()}")
    }
}

repositories {
    mavenCentral()
    mavenLocal()
    maven(url = "https://repo.spring.io/milestone")
}

dependencies {
    platform(libs.komapper.platform).let {
        implementation(it)
        ksp(it)
    }

    implementation(libs.komapper.spring.boot.starter.jdbc)
    implementation(libs.komapper.dialect.h2.jdbc)
    ksp(libs.komapper.processor)

    implementation(libs.spring.boot.starter.thymeleaf)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.thymeleaf.extras.springsecurity6)
    implementation(libs.jdbc.h2)
    implementation(libs.webjars.jquery)
    implementation(libs.webjars.locator)
    implementation(libs.thymeleaf.layout.dialect)
    developmentOnly(libs.spring.boot.devtools)
    testImplementation(libs.spring.boot.starter.test) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation(libs.spring.boot.jdbc.test)
    testImplementation(libs.spring.boot.restclient)
    testImplementation(libs.spring.boot.resttestclient)
    testImplementation(libs.komapper.spring.boot.starter.test.jdbc)
}

tasks {
    test {
        useJUnitPlatform()
    }
}
