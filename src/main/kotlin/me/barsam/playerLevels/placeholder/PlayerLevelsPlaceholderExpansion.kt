package me.barsam.playerLevels.placeholder

import me.barsam.playerLevels.PlayerLevels
import me.barsam.playerLevels.api.PlayerLevelsAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

class PlayerLevelsPlaceholderExpansion(private val plugin: JavaPlugin) : PlaceholderExpansion() {

    private val api: PlayerLevelsAPI = PlayerLevels.api

    override fun getAuthor(): String = "barsam"
    override fun getIdentifier(): String = "playerlevels"
    override fun getVersion(): String = "0.1 BETA"

    override fun onPlaceholderRequest(player: Player, identifier: String): String? {
        val uuid: UUID = player.uniqueId

        return when (identifier) {
            "level" -> api.getPlayerLevel(uuid).toString()
            "xp" -> api.getPlayerXP(uuid).toString()
            else -> {
                // Handling for top player placeholders (level)
                if (identifier.startsWith("toplevel_")) {
                    val rank = identifier.removePrefix("toplevel_").toIntOrNull()
                    if (rank != null && rank > 0) {
                        // Get the top N players
                        val topPlayers = api.getTopPlayersByLevel(10)  // Adjust number for top N
                        if (topPlayers.isNotEmpty() && rank <= topPlayers.size) {
                            val topPlayerUUID = topPlayers[rank - 1]
                            val topPlayerLevel = api.getPlayerLevel(topPlayerUUID)  // Get the level of the top player
                            return topPlayerLevel.toString()  // Return the level as a string
                        }
                    }
                    // Return "none" if there are no players or rank is out of bounds
                    return "none"
                }
                // Handling for top player placeholders (name)
                if (identifier.startsWith("topname_")) {
                    val rank = identifier.removePrefix("topname_").toIntOrNull()
                    if (rank != null && rank > 0) {
                        // Get the top N players
                        val topPlayers = api.getTopPlayersByLevel(10)  // Adjust number for top N
                        if (topPlayers.isNotEmpty() && rank <= topPlayers.size) {
                            val topPlayerUUID = topPlayers[rank - 1]
                            val topPlayer = Bukkit.getPlayer(topPlayerUUID)
                            return topPlayer?.name ?: "Unknown"
                        }
                    }
                    // Return "none" if there are no players or rank is out of bounds
                    return "none"
                }
                null
            }
        }
    }

}
