plugins {
    application
    id("com.google.devtools.ksp")
}

val komapperVersion: String by project

application {
    mainClass.set("org.komapper.example.ApplicationKt")
}

dependencies {
    implementation("org.komapper:komapper-starter-jdbc:$komapperVersion")
    implementation("org.komapper:komapper-dialect-h2-jdbc:$komapperVersion")
    ksp("org.komapper:komapper-processor:$komapperVersion")
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}

ksp {
    arg("komapper.namingStrategy", "UPPER_SNAKE_CASE")
}