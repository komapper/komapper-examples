plugins {
    application
    idea
    id("com.google.devtools.ksp")
}

val komapperVersion: String by project

sourceSets {
    main {
        java {
            srcDir("build/generated/ksp/main/kotlin")
        }
    }
}

idea.module {
    generatedSourceDirs.add(file("build/generated/ksp/main/kotlin"))
}

dependencies {
    implementation("org.komapper:komapper-starter-r2dbc:$komapperVersion")
    implementation("org.komapper:komapper-dialect-h2-r2dbc:$komapperVersion")
    implementation("org.slf4j:slf4j-api:1.7.32")
    ksp("org.komapper:komapper-processor:$komapperVersion")
}

application {
    mainClass.set("org.komapper.example.ApplicationKt")
}
