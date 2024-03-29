package de.eazypaul.soups.config

import kotlinx.serialization.Serializable

@Serializable
data class SoupsConfig(
    var enabled: Boolean = true,
    var permission: String = "unset",
    var heal: Int = 5,
    var cooldown: Int = 0,
    var blockOnCooldown: Boolean = true
)
