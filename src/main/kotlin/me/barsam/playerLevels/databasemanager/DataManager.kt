package me.barsam.playerLevels.databasemanager

import me.barsam.playerLevels.api.PlayerLevelsAPI
import me.barsam.playerLevels.managers.LevelManager
import me.barsam.playerLevels.managers.RewardManager
import java.sql.Connection
import java.sql.ResultSet
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object PlayerDataManager : PlayerLevelsAPI {
    private fun getConnection(): Connection = Database.getConnection()

    private data class PlayerCache(var level: Int, var xp: Int, var lastUpdated: Long)

    private val playerCache = ConcurrentHashMap<UUID, PlayerCache>()

    private const val CACHE_EXPIRATION_TIME = 4_000L // 10 seconds
    private val topPlayersCache = mutableListOf<UUID>()
    private var topPlayersCacheLastUpdated: Long = 0
    private const val TOP_PLAYERS_CACHE_EXPIRATION_TIME = 4_000L // Cache expires after 10 seconds

    fun loadAllPlayerData() {
        val statement = getConnection().prepareStatement("SELECT uuid, level, xp FROM player_levels")
        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            val uuid = UUID.fromString(resultSet.getString("uuid"))
            val level = resultSet.getInt("level")
            val xp = resultSet.getInt("xp")

            // Update cache with data from the database
            playerCache[uuid] = PlayerCache(level, xp, System.currentTimeMillis())
        }
    }

    // Fetch the player's level
    override fun getPlayerLevel(uuid: UUID): Int {
        val cached = playerCache[uuid]
        if (cached != null && System.currentTimeMillis() - cached.lastUpdated < CACHE_EXPIRATION_TIME) {
            return cached.level
        }

        // Fetch from database if not found in cache or cache has expired
        val statement = getConnection().prepareStatement("SELECT level FROM player_levels WHERE uuid=?")
        statement.setString(1, uuid.toString())
        val resultSet = statement.executeQuery()
        val level = if (resultSet.next()) resultSet.getInt("level") else 0

        // Update cache with the new level and current XP (if available)
        val xp = cached?.xp ?: 0
        playerCache[uuid] = PlayerCache(level, xp, System.currentTimeMillis())

        return level
    }

    override fun getTopPlayersByLevel(topN: Int): List<UUID> {
        if (System.currentTimeMillis() - topPlayersCacheLastUpdated < TOP_PLAYERS_CACHE_EXPIRATION_TIME) {
            return topPlayersCache.take(topN)
        }

        val statement = getConnection().prepareStatement(
            "SELECT uuid FROM player_levels ORDER BY level DESC LIMIT ?"
        )
        statement.setInt(1, topN)
        val resultSet = statement.executeQuery()

        val topPlayers = mutableListOf<UUID>()
        while (resultSet.next()) {
            val uuid = UUID.fromString(resultSet.getString("uuid"))
            topPlayers.add(uuid)
        }

        // Update the cache with the newly fetched top players
        topPlayersCache.clear()
        topPlayersCache.addAll(topPlayers)
        topPlayersCacheLastUpdated = System.currentTimeMillis()

        return topPlayers
    }

    // Fetch the player's XP
    override fun getPlayerXP(uuid: UUID): Int {
        val cached = playerCache[uuid]
        if (cached != null && System.currentTimeMillis() - cached.lastUpdated < CACHE_EXPIRATION_TIME) {
            return cached.xp
        }

        // Fetch from database if not found in cache or cache has expired
        val statement = getConnection().prepareStatement("SELECT xp FROM player_levels WHERE uuid=?")
        statement.setString(1, uuid.toString())
        val resultSet = statement.executeQuery()
        val xp = if (resultSet.next()) resultSet.getInt("xp") else 0

        // Update cache with the new XP and current level (if available)
        val level = cached?.level ?: 1
        playerCache[uuid] = PlayerCache(level, xp, System.currentTimeMillis())

        return xp
    }

    // Add XP to the player
    override fun addXP(uuid: UUID, xp: Int) {
        val currentXP = getPlayerXP(uuid) + xp
        val currentLevel = getPlayerLevel(uuid)
        val requiredXP = LevelManager.getRequiredXP(currentLevel)

        if (currentXP >= requiredXP) {
            levelUp(uuid, currentLevel + 1)
        } else {
            // Update XP in database
            val statement = getConnection().prepareStatement("UPDATE player_levels SET xp=? WHERE uuid=?")
            statement.setInt(1, currentXP)
            statement.setString(2, uuid.toString())
            statement.executeUpdate()

            // Immediately update cache after database change
            updateCache(uuid, currentLevel, currentXP)
        }
    }

    // Remove XP from the player
    override fun takeXP(uuid: UUID, xp: Int) {
        val currentXP = getPlayerXP(uuid) - xp
        val newXP = currentXP.coerceAtLeast(0) // Ensure XP doesn't go below 0
        val currentLevel = getPlayerLevel(uuid)

        // Update the XP in the database
        val statement = getConnection().prepareStatement("UPDATE player_levels SET xp=? WHERE uuid=?")
        statement.setInt(1, newXP)
        statement.setString(2, uuid.toString())
        statement.executeUpdate()

        // Immediately update cache after database change
        updateCache(uuid, currentLevel, newXP)
    }

    // Remove levels from the player
    override fun takeLevel(uuid: UUID, levels: Int) {
        if (levels > 0) {
            // Get the current level and subtract the given levels
            val currentLevel = getPlayerLevel(uuid)
            val newLevel = (currentLevel - levels).coerceAtLeast(1) // Ensure level doesn't go below 1

            // Update the level in the database
            val statement = getConnection().prepareStatement("UPDATE player_levels SET level=?, xp=0 WHERE uuid=?")
            statement.setInt(1, newLevel)
            statement.setString(2, uuid.toString())
            statement.executeUpdate()

            // Update the cache with the new level and reset XP
            updateCache(uuid, newLevel, 0)
        }
    }

    // Add levels to the player
    override fun addLevel(uuid: UUID, levels: Int) {
        if (levels > 0) levelUp(uuid, getPlayerLevel(uuid) + levels)
    }

    // Set the player's level
    override fun setLevel(uuid: UUID, level: Int) {
        if (level < 1) return

        // Update in database
        val statement = getConnection().prepareStatement("UPDATE player_levels SET level=?, xp=0 WHERE uuid=?")
        statement.setInt(1, level)
        statement.setString(2, uuid.toString())
        statement.executeUpdate()

        // Update the cache with the new level
        updateCache(uuid, level, 0)
    }

    // Save player data to the database
    fun saveAllPlayerData() {
        playerCache.forEach { (uuid, cache) ->
            val statement = getConnection().prepareStatement("INSERT INTO player_levels (uuid, level, xp) VALUES (?, ?, ?) ON CONFLICT (uuid) DO UPDATE SET level=?, xp=?")
            statement.setString(1, uuid.toString())
            statement.setInt(2, cache.level)
            statement.setInt(3, cache.xp)
            statement.setInt(4, cache.level)
            statement.setInt(5, cache.xp)
            statement.executeUpdate()
        }
    }

    private fun levelUp(uuid: UUID, newLevel: Int) {
        val statement = getConnection().prepareStatement("UPDATE player_levels SET level=?, xp=0 WHERE uuid=?")
        statement.setInt(1, newLevel)
        statement.setString(2, uuid.toString())
        statement.executeUpdate()

        // Immediately update the cache with the new level
        updateCache(uuid, newLevel, 0)

        // Give a reward for leveling up
        RewardManager.giveRewards(uuid, newLevel)
    }

    private fun updateCache(uuid: UUID, level: Int, xp: Int) {
        playerCache[uuid] = PlayerCache(level, xp, System.currentTimeMillis())
    }
}
