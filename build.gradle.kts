import net.wasdev.wlp.gradle.plugins.extensions.FeatureExtension
import net.wasdev.wlp.gradle.plugins.extensions.ServerExtension
import kotlin.collections.mapOf

plugins {
    kotlin("jvm") version "1.2.50"
    id("net.wasdev.wlp.gradle.plugins.Liberty")  version "2.2"
    war
}

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    providedCompile("javax", "javaee-api", "8.0")

    testImplementation("junit", "junit", "4.12")
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

val httpPort by extra { 8080 }
val httpsPort by extra { 9443 }

liberty {
    server = ServerExtension()
    server.name = "forecastServer"
    server.dropins = listOf(tasks.getByName("war"))
    server.bootstrapProperties = mapOf("httpPort" to httpPort, "httpsPort" to httpsPort)
    server.configDirectory = file("src/main/liberty/config")

    server.features = FeatureExtension()
    server.features.acceptLicense = true
}

tasks.getByName("clean").dependsOn("libertyStop")

