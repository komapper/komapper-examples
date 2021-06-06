import org.komapper.jdbc.Database

plugins {
    application
    idea
    id("com.google.devtools.ksp")
    id("org.komapper.gradle")
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
    implementation("org.komapper:komapper-starter-jdbc:$komapperVersion")
    ksp("org.komapper:komapper-processor:$komapperVersion")
}

komapper {
    generators {
        val basePackage = "org.komapper.example"
        register("mysql") {
            database.set(Database.create("jdbc:mysql://localhost/example", "root", "example"))
            packageName.set("$basePackage.mysql")
            overwriteEntities.set(true)
            overwriteDefinitions.set(true)
        }
        register("postgres") {
            database.set(Database.create("jdbc:postgresql://localhost/example", "postgres", "example"))
            packageName.set("$basePackage.postgres")
            overwriteEntities.set(true)
            overwriteDefinitions.set(true)
        }
    }
}
