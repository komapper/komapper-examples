plugins {
    application
    idea
    id("com.google.devtools.ksp")
}

val ktorVersion: String by project
val komapperVersion: String by project

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/ktor/eap")
}

dependencies {
    platform("org.komapper:komapper-platform:$komapperVersion").let {
        implementation(it)
        ksp(it)
    }
    platform("io.ktor:ktor-bom:$ktorVersion").let {
        implementation(it)
        testImplementation(it)
    }

    implementation("org.komapper:komapper-starter-r2dbc")
    implementation("org.komapper:komapper-dialect-h2-r2dbc")
    ksp("org.komapper:komapper-processor")

    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-freemarker")
    implementation("io.ktor:ktor-server-locations")
    implementation("io.ktor:ktor-server-conditional-headers")
    implementation("io.ktor:ktor-server-default-headers")
    implementation("io.ktor:ktor-server-partial-content")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("org.freemarker:freemarker:2.3.31")
    implementation("no.api.freemarker:freemarker-java8:2.0.0")
    runtimeOnly("ch.qos.logback:logback-classic:1.2.11")

    testImplementation("io.ktor:ktor-server-test-host")
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
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }
}
