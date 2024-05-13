plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
    id("io.papermc.paperweight.userdev") version "1.5.15"
    id("xyz.jpenilla.run-paper") version "2.2.3"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "de.eazypaul"
version = "1.0"

repositories {
    mavenCentral()
    maven { url = uri("https://s01.oss.sonatype.org/content/groups/public/") }
}

dependencies {
    implementation("com.charleskorn.kaml:kaml:0.58.0")

    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
    implementation("de.miraculixx:kpaper:1.2.1")

    implementation("dev.jorel:commandapi-bukkit-shade:9.3.0")
    implementation("dev.jorel:commandapi-bukkit-kotlin:9.4.1")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    shadowJar {
        mergeServiceFiles()
    }
}

bukkit {
    name = "Soups"
    apiVersion = "1.16"
    author = "eazypaulCode"
    main = "$group.soups.Soups"
    version = getVersion().toString()
}