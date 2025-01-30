package me.barsam.playerLevels.managers

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin

object LevelManager {
    private lateinit var config: FileConfiguration

    fun initialize(plugin: JavaPlugin) {
        config = plugin.config
    }

    fun getRequiredXP(level: Int): Int {
        return config.getInt("levels.$level.need_xp", 100)
    }
}
