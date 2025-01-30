package me.barsam.playerLevels.utils

import me.barsam.playerLevels.PlayerLevels
import org.bukkit.ChatColor

object ConfigManager {
    private val plugin = PlayerLevels.instance

    private val prefix: String = plugin.config.getString("prefix", "&7[PlayerLevels]")!!

    fun getMessage(key: String): String {
        val message = plugin.config.getString("messages.$key", "&cMessage not found!")!!
        return ChatColor.translateAlternateColorCodes('&', "$prefix $message")
    }
}
