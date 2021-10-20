import java.time.Year
import java.time.format.DateTimeFormatter

plugins {
    java
    `maven-publish`
    id("fabric-loom") version "0.9-SNAPSHOT"
    id("org.cadixdev.licenser") version "0.6.1"
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
    withSourcesJar()
}

group = "dev.galacticraft"
version = "0.1.0"
println("GameUnit: $version")

val minecraftVersion = "1.17.1"
val yarnBuild = "61"
val loaderVersion = "0.12.2"
val fabricVersion = "0.41.0+1.17"

repositories {
    mavenCentral()
}

/**
 * From: net.fabricmc.loom.configuration.FabricApiExtension.getDependencyNotation
 */
fun getFabricApiModule(moduleName: String): String {
    return "net.fabricmc.fabric-api:${moduleName}:${fabricApi.moduleVersion(moduleName, fabricVersion)}"
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$minecraftVersion+build.$yarnBuild:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")

    listOf(
        "fabric-api-base",
        "fabric-gametest-api-v1"
    ).forEach {
        modImplementation(getFabricApiModule(it)) { isTransitive = false }
    }
}

tasks.jar {
    from("LICENSE")
    manifest {
        attributes(
            "Implementation-Title"     to project.name,
            "Implementation-Version"   to project.version,
            "Implementation-Vendor"    to "Team Galacticraft",
            "Implementation-Timestamp" to DateTimeFormatter.ISO_DATE_TIME,
            "Maven-Artifact"           to "${project.group}:${project.name}:${project.version}"
        )
    }
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            groupId = "${project.group}"
            artifactId = project.name

            artifact(tasks.remapJar) { builtBy(tasks.remapJar) }
            artifact(tasks.getByName("sourcesJar", Jar::class)) { builtBy(tasks.remapSourcesJar) }
        }
    }
    repositories {
        val isSnapshot: Boolean = System.getenv("SNAPSHOT")?.equals("true") ?: true
        val mavenBase = "https://maven.galacticraft.dev/"
        maven(if(isSnapshot) "$mavenBase/snapshots" else mavenBase) {
            name = "maven"
            credentials(PasswordCredentials::class)
            authentication {
                register("basic", BasicAuthentication::class)
            }
        }
    }
}

license {
    setHeader(project.file("LICENSE_HEADER.txt"))
    include("**/dev/galacticraft/**/*.java")
    include("build.gradle.kts")
    ext {
        set("year", Year.now().value)
        set("company", "Team Galacticraft")
    }
}

tasks.withType(JavaCompile::class) {
    dependsOn(tasks.checkLicenses)
    options.encoding = "UTF-8"
    options.release.set(16)
}