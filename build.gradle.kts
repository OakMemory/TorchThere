import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.papermc.paperweight.userdev") version "1.4.0"
}

group = "dev.krysztal"
version = "1.0-SNAPSHOT"

val targetJavaVersion = 17
val compileKotlin: KotlinCompile by tasks
val compileTestKotlin: KotlinCompile by tasks


repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    val paper_version : String by project
    val lombok_version : String by project

    paperDevBundle(paper_version)

    compileOnly("org.projectlombok:lombok:${lombok_version}")
    annotationProcessor("org.projectlombok:lombok:${lombok_version}")

    testCompileOnly("org.projectlombok:lombok:${lombok_version}")
    testAnnotationProcessor("org.projectlombok:lombok:${lombok_version}")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks.processResources {
    inputs.property("version", version)
    filesMatching("plugin.yml") { expand(mutableMapOf("version" to version)) }
}

compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}