plugins {
    kotlin("jvm") version "1.9.22"
    `maven-publish`
}

group = "org.jukeboxmc.mapping"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
        repositories {
            maven {
                url = uri("https://repo.jukeboxmc.eu/snapshots")
                credentials.username = properties["username"].toString()
                credentials.password = properties["password"].toString()
            }
        }
    }
}