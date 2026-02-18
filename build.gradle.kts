plugins {
    `java-library`
}

group = "de.fabiexe"
version = "0.0.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
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
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks {
    test {
        useJUnitPlatform()
    }
}