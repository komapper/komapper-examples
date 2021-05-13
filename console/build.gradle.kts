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
    implementation("org.komapper:komapper-starter:$komapperVersion")
    ksp("org.komapper:komapper-processor:$komapperVersion")
}

application {
    mainClass.set("org.komapper.example.ApplicationKt")
}
