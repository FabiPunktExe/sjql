plugins {
    `java-library`
}

group = "de.fabiexe"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.annotations)
    testCompileOnly(libs.annotations)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.h2)
    testImplementation(libs.sqlite)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks {
    test {
        useJUnitPlatform()
    }
}