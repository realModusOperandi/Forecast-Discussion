import io.openliberty.tools.gradle.extensions.ServerExtension
import kotlin.collections.mapOf

plugins {
    kotlin("jvm") version "1.4.32"
    id("io.openliberty.tools.gradle.Liberty") version "3.2"
    war
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.32")
    providedCompile("javax", "javaee-api", "8.0")

    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.6.2")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.6.2")

    libertyRuntime("io.openliberty", "openliberty-runtime", "[21.0.0.6,)")
}

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
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

