plugins {
    kotlin("jvm") version "2.2.0"
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.rozetkapay"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.librepdf:openpdf:2.2.2")
    implementation("com.github.librepdf:openpdf-html:2.2.2")
    implementation("com.github.librepdf:openpdf-kotlin:2.2.2")
    implementation("org.slf4j:slf4j-simple:2.0.13")
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("com.rozetkapay.pdfcli.MainKt")
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

kotlin {
    jvmToolchain(21)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.shadowJar {
    archiveBaseName.set("pdfcli")
    archiveClassifier.set("")
    archiveVersion.set("")
}

// JVM Application configuration
// To run: ./gradlew run --args "--in input.html --out output.pdf"