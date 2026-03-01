plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
}

group = "de.fabiexe"
version = "0.3.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.annotations)
    testCompileOnly(libs.annotations)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.h2)
    testImplementation(libs.sqlite)
    testImplementation(libs.postgresql)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks {
    test {
        useJUnitPlatform()
    }
}

publishing {
    repositories {
        maven("https://repo.diruptio.de/repository/maven-public-releases") {
            name = "DiruptioPublic"
            credentials {
                username = (System.getenv("DIRUPTIO_REPO_USERNAME") ?: project.findProperty("maven_username") ?: "").toString()
                password = (System.getenv("DIRUPTIO_REPO_PASSWORD") ?: project.findProperty("maven_password") ?: "").toString()
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            artifactId = "sjql"
            from(components["java"])
        }
    }
}