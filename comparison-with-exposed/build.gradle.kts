plugins {
    application
    alias(libs.plugins.ksp)
}

application {
    mainClass.set("org.komapper.example.ApplicationKt")
}

dependencies {
    platform(libs.komapper.platform).let {
        implementation(it)
        ksp(it)
    }
    implementation(libs.komapper.starter.jdbc)
    implementation(libs.komapper.dialect.h2.jdbc)
    ksp(libs.komapper.processor)
}

ksp {
    arg("komapper.namingStrategy", "UPPER_SNAKE_CASE")
}
