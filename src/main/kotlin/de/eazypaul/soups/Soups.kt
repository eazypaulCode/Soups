package de.eazypaul.soups

import de.eazypaul.soups.command.SoupsCommand
import de.eazypaul.soups.component.cmp
import de.eazypaul.soups.component.plus
import de.eazypaul.soups.config.ConfigLoader
import de.eazypaul.soups.listener.SoupListener
import de.miraculixx.kpaper.main.KPaper
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import net.kyori.adventure.text.format.NamedTextColor

class Soups : KPaper() {

    private val config = ConfigLoader()

    override fun load() {
        CommandAPI.onLoad(
            CommandAPIBukkitConfig(this).silentLogs(true)
        )
        config.load(this)
    }

    override fun startup() {
        CommandAPI.onEnable()
        componentLogger.info(cmp("Thank you for using Soups!", NamedTextColor.GREEN))
        componentLogger.info(
            cmp("Minecraft Version: ") +
                    cmp(server.minecraftVersion, NamedTextColor.GREEN) +
                    cmp(" | Plugin Version: ") +
                    cmp(description.version, NamedTextColor.GREEN)
        ) // TODO: Fix deprecation
        SoupListener(config.config)
        SoupsCommand(description.version, server.minecraftVersion, config)
    }

    override fun shutdown() {
        CommandAPI.unregister("soups")
        CommandAPI.onDisable()
    }

}