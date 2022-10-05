import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")

    implementation("org.spongepowered:configurate-yaml:4.1.2")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
    }

    withType<ShadowJar> {
        fun reloc(vararg clazz: String) {
            clazz.forEach { relocate(it, "${project.group}.${rootProject.name}.libs.$it") }
        }

        reloc(
            "org.spongepowered"
        )

        archiveFileName.set("HalloweenThings-${project.version}.jar")
    }

    build {
        dependsOn(shadowJar)
    }
}

bukkit {
    name = "HalloweenThings"
    version = "${project.version}"
    main = "${project.group}.${rootProject.name}.HalloweenThingsPlugin"
    apiVersion = "1.19"
}
