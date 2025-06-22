plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

version = "1.1.0"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    implementation("com.sun.xml.ws:jaxws-rt:4.0.3")
    implementation("com.thoughtworks.xstream:xstream:1.4.20")
    // https://mvnrepository.com/artifact/commons-io/commons-io
    implementation("commons-io:commons-io:2.12.0")


    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.1-jre")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    // Define the main class for the application.
    mainClass.set("dev.joguenco.Main")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

tasks.jar {
    // Customize the JAR file name
    archiveFileName.set("RoQuiClientSri-$version.jar")
}