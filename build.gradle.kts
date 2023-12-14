group = "dev.luna5ama"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.9.21"
}

apply {
    plugin("kotlin")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("it.unimi.dsi:fastutil:8.5.11")
}