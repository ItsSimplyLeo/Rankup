plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.papermc.paperweight.userdev") version "1.5.2"
}

group = "cx.leo.rankup"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://ci.ender.zone/plugin/repository/everything/")
}

dependencies {
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")

    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    implementation("dev.triumphteam:triumph-gui:3.1.5")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    shadowJar {
        relocate("dev.triumphteam.gui", "cx.leo.rankup.lib.gui")
    }

    build {
        dependsOn(shadowJar)
    }
}