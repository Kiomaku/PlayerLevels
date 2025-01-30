package me.barsam.playerLevels.api

import java.util.UUID

interface PlayerLevelsAPI {

    // Gets the player's current level
    fun getPlayerLevel(uuid: UUID): Int

    // Gets the player's current XP
    fun getPlayerXP(uuid: UUID): Int

    // Adds XP to a player
    fun addXP(uuid: UUID, xp: Int)

    // Removes XP from a player
    fun takeXP(uuid: UUID, xp: Int)

    // Adds levels to a player
    fun addLevel(uuid: UUID, levels: Int)

    // Removes levels from a player
    fun takeLevel(uuid: UUID, levels: Int)

    // Sets the player's level
    fun setLevel(uuid: UUID, level: Int)

    // Gets top player by an int 1,2,3 ETC.
    fun getTopPlayersByLevel(topN: Int): List<UUID>
}
