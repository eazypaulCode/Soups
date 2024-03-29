package de.eazypaul.soups.component

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

operator fun Component.plus(other: Component): Component = append(other)

fun cmp(text: String, color: NamedTextColor = NamedTextColor.GRAY): Component = Component.text(text, color)