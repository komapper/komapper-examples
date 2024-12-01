plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.spring)
}

apply(plugin = libs.plugins.spring.dependency.management.get().pluginId)

springBoot {
    mainClass.set("org.komapper.example.ApplicationKt")
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
}

tasks {
    test {
        useJUnitPlatform()
    }
}
