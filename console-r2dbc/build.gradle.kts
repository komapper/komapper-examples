plugins {
    application
    alias(libs.plugins.ksp)
}

dependencies {
    platform(libs.komapper.platform).let {
        implementation(it)
        ksp(it)
    }
    implementation(libs.komapper.starter.r2dbc)
    implementation(libs.komapper.dialect.h2.r2dbc)
    ksp(libs.komapper.processor)
}

application {
    mainClass.set("org.komapper.example.ApplicationKt")
}
