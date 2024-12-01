plugins {
    application
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
}

dependencies {
    platform(libs.komapper.platform).let {
        implementation(it)
        ksp(it)
    }
    platform(libs.ktor.bom).let {
        implementation(it)
        testImplementation(it)
    }

    implementation(libs.komapper.starter.r2dbc)
    implementation(libs.komapper.dialect.h2.r2dbc)
    ksp(libs.komapper.processor)

    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.freemarker)
    implementation(libs.ktor.server.resources)
    implementation(libs.ktor.server.sessions)
    implementation(libs.ktor.server.conditional.headers)
    implementation(libs.ktor.server.default.headers)
    implementation(libs.ktor.server.partial.content)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.freemarker)
    implementation(libs.freemarker.java8)
    runtimeOnly(libs.logback.classic)

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlin.test)
}

sourceSets {
    main {
        kotlin.srcDirs("src")
        resources.srcDir("resources")
    }
    test {
        kotlin.srcDirs("test")
    }
}

ksp {
    arg("komapper.namingStrategy", "lower_snake_case")
}
