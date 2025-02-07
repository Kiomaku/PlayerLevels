package me.barsam.playerLevels.managers

import me.barsam.playerLevels.PlayerLevels

object LevelManager {
    fun getRequiredXP(level: Int): Int {
        return PlayerLevels.instance.getCachedConfigValue("levels.$level.need_xp") as? Int ?: 100
    }
}
