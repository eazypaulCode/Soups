package de.eazypaul.soups.listener

import de.eazypaul.soups.config.SoupsConfig
import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.runnables.taskRunLater
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.util.UUID

class SoupListener(config: SoupsConfig) {

    private val onCooldown = mutableListOf<UUID>()

    val onSoup = listen<PlayerInteractEvent> {
        if(!it.action.isRightClick) return@listen

        if (!config.enabled) return@listen

        val player = it.player

        val permission = config.permission
        if (!permission.equals("unset", true) && !player.hasPermission(permission)) return@listen

        val cooldown = config.cooldown
        if (cooldown != 0 && onCooldown.contains(player.uniqueId)) {
            if (config.blockOnCooldown) it.isCancelled = true
            return@listen
        }

        val item = it.item ?: return@listen
        if (item.type != Material.MUSHROOM_STEW) return@listen

        val maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)
        player.health = (player.health + config.heal).coerceAtMost(maxHealth?.value ?: maxHealth?.defaultValue ?: 20.0)
        player.playSound(player, Sound.ENTITY_PLAYER_BURP, 1f, 1f)

        player.foodLevel = (player.foodLevel + 6).coerceAtMost(20)

        if (item.amount == 1) {
            item.type = Material.BOWL
        } else {
            item.amount--
            val added = player.inventory.addItem(ItemStack(Material.BOWL)).isEmpty()
            if (!added) player.world.dropItem(player.location, ItemStack(Material.BOWL))
        }

        onCooldown.add(player.uniqueId)
        taskRunLater(cooldown * 20L) {
            onCooldown.remove(player.uniqueId)
        }
        it.isCancelled = true
    }

}