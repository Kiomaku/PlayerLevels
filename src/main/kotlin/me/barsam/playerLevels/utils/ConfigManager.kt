package me.barsam.playerLevels.utils

import me.barsam.playerLevels.PlayerLevels
import org.bukkit.ChatColor

object ConfigManager {
    private val plugin = PlayerLevels.instance

    private var prefix: String = "&7[PlayerLevels]"

    init {
        reload()
    }

    fun getMessage(key: String): String {
        val message = PlayerLevels.instance.getCachedConfigValue("messages.$key") as? String ?: "&cMessage not found!"
        return ChatColor.translateAlternateColorCodes('&', "$prefix $message")
    }

    fun reload() {
        prefix = PlayerLevels.instance.getCachedConfigValue("prefix") as? String ?: "&7[PlayerLevels]"
    }
}