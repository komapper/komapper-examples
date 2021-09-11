plugins {
    application
    id("com.google.devtools.ksp")
}

val komapperVersion: String by project

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}

dependencies {
    implementation("org.komapper:komapper-starter-r2dbc:$komapperVersion")
    implementation("org.komapper:komapper-dialect-h2-r2dbc:$komapperVersion")
    ksp("org.komapper:komapper-processor:$komapperVersion")
}

application {
    mainClass.set("org.komapper.example.ApplicationKt")
}
