plugins {
    idea
    id("io.quarkus")
    id("com.google.devtools.ksp")
    kotlin("plugin.allopen")
}

val quarkusVersion: String by project
val komapperVersion: String by project

dependencies {
    implementation(enforcedPlatform("io.quarkus:quarkus-bom:$quarkusVersion"))
    implementation(platform("org.komapper:komapper-platform:$komapperVersion"))
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-jdbc-postgresql")
    implementation("io.quarkus:quarkus-resteasy")
    implementation("io.quarkus:quarkus-resteasy-jsonb")
    implementation("org.komapper:komapper-quarkus-jdbc")
    implementation("org.komapper:komapper-dialect-postgresql-jdbc")
    ksp("org.komapper:komapper-processor:$komapperVersion")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured:4.5.0")
}

idea {
    module {
        sourceDirs = sourceDirs + file("build/generated/ksp/main/kotlin")
        testSourceDirs = testSourceDirs + file("build/generated/ksp/test/kotlin")
        generatedSourceDirs =
            generatedSourceDirs + file("build/generated/ksp/main/kotlin") + file("build/generated/ksp/test/kotlin")
    }
}

ksp {
    arg("komapper.namingStrategy", "UPPER_SNAKE_CASE")
}

allOpen {
    annotation("javax.ws.rs.Path")
    annotation("javax.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
}
