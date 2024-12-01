plugins {
    alias(libs.plugins.quarkus)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.allopen)
}

dependencies {
    platform(libs.komapper.platform).let {
        implementation(it)
        ksp(it)
    }
    implementation(enforcedPlatform(libs.quarkus.bom))
    implementation(libs.quarkus.kotlin)
    implementation(libs.quarkus.jdbc.postgresql)
    implementation(libs.quarkus.resteasy)
    implementation(libs.quarkus.resteasy.jsonb)
    implementation(libs.komapper.quarkus.jdbc)
    implementation(libs.komapper.dialect.postgresql.jdbc)
    ksp(libs.komapper.processor)
    testImplementation(libs.quarkus.junit5)
    testImplementation(libs.rest.assured)
    testImplementation(libs.quarkus.jdbc.h2)
    testImplementation(libs.komapper.dialect.h2.jdbc)
}

ksp {
    arg("komapper.namingStrategy", "UPPER_SNAKE_CASE")
}

allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
}
