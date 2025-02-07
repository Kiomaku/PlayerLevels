package me.barsam.playerLevels

import me.barsam.playerLevels.api.PlayerLevelsAPI
import me.barsam.playerLevels.commands.PlayerLevelsCommand
import me.barsam.playerLevels.databasemanager.Database
import me.barsam.playerLevels.databasemanager.PlayerDataManager
import me.barsam.playerLevels.managers.LevelManager
import me.barsam.playerLevels.managers.RewardManager
import me.barsam.playerLevels.placeholder.PlayerLevelsPlaceholderExpansion
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin

class PlayerLevels : JavaPlugin() {

    companion object {
        lateinit var instance: PlayerLevels
            private set

        // Expose the API to other plugins
        val api: PlayerLevelsAPI = PlayerDataManager
    }

    private val configCache: MutableMap<String, Any> = mutableMapOf()

    override fun onEnable() {
        instance = this

        // Register PlaceholderAPI expansion if available
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            PlayerLevelsPlaceholderExpansion(this).register()
        } else {
            logger.warning("PlaceholderAPI not found! The placeholder expansion will not work.")
        }

        // Save default config to ensure the config is loaded
        saveDefaultConfig()
        cacheConfig()

        // Initialize the database connection
        Database.initDatabase()

        // Initialize RewardManager with cached config and Vault economy
        val economy = if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            Bukkit.getServicesManager().getRegistration(Economy::class.java)?.provider
        } else {
            null
        }
        RewardManager.initialize(config, economy)

        // Load all player data from the database
        PlayerDataManager.loadAllPlayerData()

        // Register commands
        getCommand("playerlevels")?.setExecutor(PlayerLevelsCommand())
        getCommand("playerlevels")?.setTabCompleter(PlayerLevelsCommand())

        logger.info("PlayerLevels Plugin Enabled!")
    }

    override fun onDisable() {
        // Save all player data when the plugin is disabled
        PlayerDataManager.saveAllPlayerData()

        // Close the database connection
        Database.closeConnection()

        logger.info("PlayerLevels Plugin Disabled!")
    }

    private fun cacheConfig() {
        configCache.clear()
        for (key in config.getKeys(true)) {
            configCache[key] = config.get(key) ?: continue
        }
    }

    fun getCachedConfigValue(path: String): Any? {
        return configCache[path]
    }

    fun reloadPluginConfig() {
        reloadConfig()
        cacheConfig()
        RewardManager.initialize(config, Bukkit.getServicesManager().getRegistration(Economy::class.java)?.provider)
        logger.info("Configuration reloaded successfully!")
    }
}