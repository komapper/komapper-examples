plugins {
    application
    idea
    id("com.google.devtools.ksp")
}

val komapperVersion: String by project

dependencies {
    platform("org.komapper:komapper-platform:$komapperVersion").let {
        implementation(it)
        ksp(it)
    }
    implementation("org.komapper:komapper-starter-r2dbc")
    implementation("org.komapper:komapper-dialect-h2-r2dbc")
    ksp("org.komapper:komapper-processor")
}

application {
    mainClass.set("org.komapper.example.ApplicationKt")
}
