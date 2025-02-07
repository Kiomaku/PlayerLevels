package me.barsam.playerLevels.databasemanager

import me.barsam.playerLevels.api.PlayerLevelsAPI
import me.barsam.playerLevels.managers.LevelManager
import me.barsam.playerLevels.managers.RewardManager
import java.sql.Connection
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object PlayerDataManager : PlayerLevelsAPI {
    private fun getConnection(): Connection = Database.getConnection()

    private data class PlayerCache(var level: Int, var xp: Int)

    private val playerCache = ConcurrentHashMap<UUID, PlayerCache>()

    fun loadAllPlayerData() {
        val statement = getConnection().prepareStatement("SELECT uuid, level, xp FROM player_levels")
        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            val uuid = UUID.fromString(resultSet.getString("uuid"))
            val level = resultSet.getInt("level")
            val xp = resultSet.getInt("xp")
            playerCache[uuid] = PlayerCache(level, xp)
        }
    }
    override fun getTopPlayersByLevel(topN: Int): List<UUID> {
        return playerCache.entries
            .sortedByDescending { it.value.level }
            .take(topN)
            .map { it.key }
    }

    override fun getPlayerLevel(uuid: UUID): Int {
        return playerCache[uuid]?.level ?: 1
    }

    override fun getPlayerXP(uuid: UUID): Int {
        return playerCache[uuid]?.xp ?: 0
    }

    override fun addXP(uuid: UUID, xp: Int) {
        val playerData = playerCache.computeIfAbsent(uuid) { PlayerCache(1, 0) }
        playerData.xp += xp
        val requiredXP = LevelManager.getRequiredXP(playerData.level)

        if (playerData.xp >= requiredXP) {
            levelUp(uuid, playerData.level + 1)
        }
    }

    override fun takeXP(uuid: UUID, xp: Int) {
        val playerData = playerCache.computeIfAbsent(uuid) { PlayerCache(1, 0) }
        playerData.xp = (playerData.xp - xp).coerceAtLeast(0)
    }

    override fun takeLevel(uuid: UUID, levels: Int) {
        if (levels > 0) {
            val playerData = playerCache.computeIfAbsent(uuid) { PlayerCache(1, 0) }
            playerData.level = (playerData.level - levels).coerceAtLeast(1)
            playerData.xp = 0
        }
    }

    override fun addLevel(uuid: UUID, levels: Int) {
        if (levels > 0) levelUp(uuid, getPlayerLevel(uuid) + levels)
    }

    override fun setLevel(uuid: UUID, level: Int) {
        if (level < 1) return
        val playerData = playerCache.computeIfAbsent(uuid) { PlayerCache(1, 0) }
        playerData.level = level
        playerData.xp = 0
    }

    fun saveAllPlayerData() {
        val connection = getConnection()
        val statement = connection.prepareStatement("INSERT INTO player_levels (uuid, level, xp) VALUES (?, ?, ?) ON CONFLICT (uuid) DO UPDATE SET level=?, xp=?")

        for ((uuid, cache) in playerCache) {
            statement.setString(1, uuid.toString())
            statement.setInt(2, cache.level)
            statement.setInt(3, cache.xp)
            statement.setInt(4, cache.level)
            statement.setInt(5, cache.xp)
            statement.executeUpdate()
        }
    }

    private fun levelUp(uuid: UUID, newLevel: Int) {
        val playerData = playerCache.computeIfAbsent(uuid) { PlayerCache(1, 0) }
        playerData.level = newLevel
        playerData.xp = 0
        RewardManager.giveRewards(uuid, newLevel)
    }
}