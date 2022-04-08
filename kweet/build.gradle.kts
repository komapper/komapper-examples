plugins {
    application
    idea
    id("com.google.devtools.ksp")
}

val komapperVersion: String by project

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/ktor/eap")
}

dependencies {
    implementation("org.komapper:komapper-starter-r2dbc:$komapperVersion")
    implementation("org.komapper:komapper-dialect-h2-r2dbc:$komapperVersion")
    ksp("org.komapper:komapper-processor:$komapperVersion")

    implementation("io.ktor:ktor-server-netty:2.0.0-eap-256")
    implementation("io.ktor:ktor-server-freemarker:2.0.0-eap-256")
    implementation("io.ktor:ktor-server-locations:2.0.0-eap-256")
    implementation("io.ktor:ktor-server-conditional-headers:2.0.0-eap-256")
    implementation("io.ktor:ktor-server-default-headers:2.0.0")
    implementation("io.ktor:ktor-server-partial-content:2.0.0-eap-256")
    implementation("io.ktor:ktor-server-call-logging:2.0.0-eap-256")
    implementation("org.freemarker:freemarker:2.3.31")
    implementation("no.api.freemarker:freemarker-java8:2.0.0")
    runtimeOnly("ch.qos.logback:logback-classic:1.2.11")

    testImplementation("io.ktor:ktor-server-test-host:2.0.0-eap-256")
    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

idea {
    module {
        sourceDirs = sourceDirs + file("build/generated/ksp/main/kotlin")
        testSourceDirs = testSourceDirs + file("build/generated/ksp/test/kotlin")
        generatedSourceDirs = generatedSourceDirs + file("build/generated/ksp/main/kotlin") + file("build/generated/ksp/test/kotlin")
    }
}

kotlin {
    sourceSets.main {
        kotlin.srcDirs("src")
    }
    sourceSets.test {
        kotlin.srcDirs("test")
    }
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
    }
}

sourceSets {
    main {
        resources.srcDir("resources")
    }
}

ksp {
    arg("komapper.namingStrategy", "lower_snake_case")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }
}
