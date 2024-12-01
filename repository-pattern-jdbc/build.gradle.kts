plugins {
    application
    alias(libs.plugins.ksp)
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
