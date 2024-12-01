plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.ksp)
    alias(libs.plugins.graalvm.buildtools.native)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    platform(libs.komapper.platform).let {
        implementation(it)
        ksp(it)
    }
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.komapper.spring.boot.starter.r2dbc)
    implementation(libs.komapper.dialect.h2.r2dbc)
    ksp(libs.komapper.processor)
    testImplementation(libs.spring.boot.starter.test)
}

springBoot {
    mainClass.set("org.komapper.example.ApplicationKt")
}
