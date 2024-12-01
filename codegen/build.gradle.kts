buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(platform(libs.testcontainers.bom))
        classpath(libs.testcontainers.mysql)
        classpath(libs.testcontainers.postgresql)
        classpath(libs.jdbc.mysql)
        classpath(libs.jdbc.postgresql)
    }
}

plugins {
    application
    alias(libs.plugins.ksp)
    alias(libs.plugins.komapper)
}

dependencies {
    platform(libs.komapper.platform).let {
        implementation(it)
        ksp(it)
    }
    implementation(libs.komapper.starter.jdbc)
    ksp(libs.komapper.processor)
}

komapper {
    generators {
        val basePackage = "org.komapper.example"
        register("mysql") {
            jdbc {
                val initScript = file("src/main/resources/init_mysql.sql")
                driver.set("org.testcontainers.jdbc.ContainerDatabaseDriver")
                url.set("jdbc:tc:mysql:8.0.25:///test?TC_INITSCRIPT=file:${initScript.absolutePath}")
                user.set("test")
                password.set("test")
            }
            packageName.set("$basePackage.mysql")
            overwriteEntities.set(true)
            overwriteDefinitions.set(true)
        }
        register("postgresql") {
            jdbc {
                val initScript = file("src/main/resources/init_postgresql.sql")
                driver.set("org.testcontainers.jdbc.ContainerDatabaseDriver")
                url.set("jdbc:tc:postgresql:13.3:///test?TC_INITSCRIPT=file:${initScript.absolutePath}")
                user.set("test")
                password.set("test")
            }
            packageName.set("$basePackage.postgres")
            overwriteEntities.set(true)
            overwriteDefinitions.set(true)
        }
    }
}

tasks {
    named("clean") {
        doLast {
            delete("src/main/kotlin")
        }
    }
}
