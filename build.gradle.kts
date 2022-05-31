import io.openliberty.tools.gradle.extensions.ServerExtension
import kotlin.collections.mapOf

plugins {
    kotlin("jvm") version "1.6.10"
    id("io.openliberty.tools.gradle.Liberty") version "3.3.1"
    war
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")
    providedCompile("javax", "javaee-api", "8.0")

    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.8.2")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.8.2")

    libertyRuntime("io.openliberty", "openliberty-runtime", "[22.0.0.5,)")
}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val httpPort by extra { "9080" }
val httpsPort by extra { "9443" }
val applicationName by extra { (tasks["war"] as War).archiveFileName.get() }

liberty {
    server = ServerExtension()
    server.name = "forecastServer"
    server.deploy.apps = listOf(tasks["war"])
    server.bootstrapProperties = mapOf("httpPort" to httpPort, "httpsPort" to httpsPort, "applicationName" to applicationName).toProperties()
    server.configDirectory = file("src/main/liberty/config")
}

war {
    val war = tasks["war"] as War
    war.archiveFileName.set("${war.archiveBaseName.get()}.${war.archiveExtension.get()}")
}

tasks["clean"].dependsOn("libertyStop")

