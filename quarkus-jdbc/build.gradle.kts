plugins {
    idea
    id("io.quarkus")
    id("com.google.devtools.ksp")
    kotlin("plugin.allopen")
}

val quarkusVersion: String by project
val komapperVersion: String by project

dependencies {
    platform("org.komapper:komapper-platform:$komapperVersion").let {
        implementation(it)
        ksp(it)
    }
    implementation(enforcedPlatform("io.quarkus:quarkus-bom:$quarkusVersion"))
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-jdbc-postgresql")
    implementation("io.quarkus:quarkus-resteasy")
    implementation("io.quarkus:quarkus-resteasy-jsonb")
    implementation("org.komapper:komapper-quarkus-jdbc")
    implementation("org.komapper:komapper-dialect-postgresql-jdbc")
    ksp("org.komapper:komapper-processor")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured:5.4.0")
    testImplementation("io.quarkus:quarkus-jdbc-h2")
    testImplementation("org.komapper:komapper-dialect-h2-jdbc")
}

ksp {
    arg("komapper.namingStrategy", "UPPER_SNAKE_CASE")
}

allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
}
