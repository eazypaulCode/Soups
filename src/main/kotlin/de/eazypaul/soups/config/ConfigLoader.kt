package de.eazypaul.soups.config

import com.charleskorn.kaml.MalformedYamlException
import com.charleskorn.kaml.Yaml
import de.eazypaul.soups.component.cmp
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class ConfigLoader {

    lateinit var config: SoupsConfig
    private val yaml = Yaml.default
    private val file = File("plugins/Soups/config.yml")

    fun load(plugin: JavaPlugin) {
        plugin.dataFolder.mkdirs()
        if (!file.exists()) {
            plugin.componentLogger.warn(cmp("Creating default config file. Unless you are using this plugin for the first time this is a bug.", NamedTextColor.YELLOW))
            file.createNewFile()
            config = SoupsConfig()
            save()
        }

        config = try {
            yaml.decodeFromString(file.readText())
        } catch (e: MalformedYamlException) {
            plugin.componentLogger.error(cmp("Error loading configuration file because of malformed Yaml! Default settings are being used.", NamedTextColor.RED))
            SoupsConfig()
        }

        save()
    }

    fun save() {
        file.writeText(yaml.encodeToString(config))
    }

}