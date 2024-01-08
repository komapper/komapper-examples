plugins {
    application
    idea
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization")
}

val ktorVersion: String by project
val komapperVersion: String by project

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
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
    implementation("io.ktor:ktor-server-resources")
    implementation("io.ktor:ktor-server-sessions")
    implementation("io.ktor:ktor-server-conditional-headers")
    implementation("io.ktor:ktor-server-default-headers")
    implementation("io.ktor:ktor-server-partial-content")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("org.freemarker:freemarker:2.3.32")
    implementation("no.api.freemarker:freemarker-java8:2.1.0")
    runtimeOnly("ch.qos.logback:logback-classic:1.4.14")

    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
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
