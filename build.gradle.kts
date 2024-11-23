plugins {
    kotlin("jvm") version "2.0.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21" apply false
}

group = "com.example"
version = "0.0.1"

allprojects {
    repositories {
        mavenCentral()
    }
}