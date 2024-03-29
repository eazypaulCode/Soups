package de.eazypaul.soups.command

import de.eazypaul.soups.component.cmp
import de.eazypaul.soups.component.plus
import de.eazypaul.soups.config.ConfigLoader
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.kotlindsl.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor

class SoupsCommand(version: String, serverVersion: String, config: ConfigLoader) {

    private val command = commandTree("soups") {
        anyExecutor { sender, _ ->
            sender.sendMessage(cmp("Soups Version: ") + cmp(version, NamedTextColor.GREEN) + cmp(" | ") + cmp("Minecraft Version: ") + cmp(serverVersion, NamedTextColor.GREEN) + Component.newline() + cmp("Download: ") + cmp("[Modrinth]", NamedTextColor.GREEN).clickEvent(
                ClickEvent.openUrl("https://modrinth.com/project/soups")))
        }
        literalArgument("setting") {
            withPermission("soups.command.setting")
            applySetting("enabled", { config.config.enabled }, { newValue -> config.config.enabled = newValue }, config)
            applySetting("permission", { config.config.permission }, { newValue -> config.config.permission = newValue }, config, "unset")
            applySetting("heal", { config.config.heal }, { newValue -> config.config.heal = newValue }, config)
            applySetting("cooldown", { config.config.cooldown }, { newValue -> config.config.cooldown = newValue }, config)
            applySetting("blockOnCooldown", { config.config.blockOnCooldown }, { newValue -> config.config.blockOnCooldown = newValue }, config)
        }
    }

    private fun <T> Argument<*>.applySetting(name: String, currentConsumer: () -> T, consumer: (T) -> Unit, config: ConfigLoader, vararg suggestions: String) {
        literalArgument(name) {
            anyExecutor { sender, _ ->
                sender.sendMessage(cmp(name, NamedTextColor.GREEN) + cmp(" is currently set to ") + cmp(currentConsumer.invoke().toString(), NamedTextColor.GREEN))
            }

            when (currentConsumer.invoke()) {
                is Boolean -> booleanArgument("new-value") {
                    applyValue(name, consumer, config)
                }

                is Int -> integerArgument("new-value", min = 0) {
                    applyValue(name, consumer, config)
                }

                is String -> textArgument("new-value") {
                    if (suggestions.isNotEmpty()) includeSuggestions(ArgumentSuggestions.strings(*suggestions))
                    applyValue(name, consumer, config)
                }
            }
        }
    }

    private fun <T> Argument<*>.applyValue(name: String, consumer: (T) -> Unit, config: ConfigLoader) {
        anyExecutor { sender, args ->
            val new = args[0] as T
            consumer.invoke(new)
            sender.sendMessage(cmp(name, NamedTextColor.GREEN) + cmp(" successfully set to ") + cmp(new.toString(), NamedTextColor.GREEN))
            config.save()
        }
    }

}