package me.barsam.playerLevels.commands

import me.barsam.playerLevels.databasemanager.PlayerDataManager
import me.barsam.playerLevels.utils.ConfigManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil
import java.util.*

class PlayerLevelsCommand : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(ConfigManager.getMessage("usage"))
            return true
        }

        if (sender !is Player) {
            sender.sendMessage(ConfigManager.getMessage("only-players"))
            return true
        }

        val player: Player = sender

        if (!player.hasPermission("playerlevels.use")) {
            player.sendMessage(ConfigManager.getMessage("no-permission"))
            return true
        }

        when (args[0].lowercase()) {
            "addxp", "takexp", "addlevel", "takelevel" -> {
                if (args.size != 3) {
                    player.sendMessage("§cUsage: /playerlevels ${args[0]} <player> <amount>")
                    return true
                }

                val target = Bukkit.getPlayer(args[1])
                if (target == null) {
                    player.sendMessage(ConfigManager.getMessage("player-not-found"))
                    return true
                }

                val amount = args[2].toIntOrNull()
                if (amount == null || amount < 0) {
                    player.sendMessage(ConfigManager.getMessage("invalid-amount"))
                    return true
                }

                val uuid = target.uniqueId

                when (args[0].lowercase()) {
                    "addxp" -> {
                        if (!player.hasPermission("playerlevels.addxp")) {
                            player.sendMessage(ConfigManager.getMessage("no-permission"))
                            return true
                        }
                        PlayerDataManager.addXP(uuid, amount)
                        player.sendMessage(ConfigManager.getMessage("xp-received").replace("{amount}", amount.toString()))
                    }
                    "takexp" -> {
                        if (!player.hasPermission("playerlevels.takexp")) {
                            player.sendMessage(ConfigManager.getMessage("no-permission"))
                            return true
                        }
                        PlayerDataManager.takeXP(uuid, amount)
                        player.sendMessage(ConfigManager.getMessage("xp-removed").replace("{amount}", amount.toString()))
                    }
                    "addlevel" -> {
                        if (!player.hasPermission("playerlevels.addlevel")) {
                            player.sendMessage(ConfigManager.getMessage("no-permission"))
                            return true
                        }
                        PlayerDataManager.addLevel(uuid, amount)
                        player.sendMessage(ConfigManager.getMessage("level-gained").replace("{amount}", amount.toString()))
                    }
                    "takelevel" -> {
                        if (!player.hasPermission("playerlevels.takelevel")) {
                            player.sendMessage(ConfigManager.getMessage("no-permission"))
                            return true
                        }
                        PlayerDataManager.takeLevel(uuid, amount)
                        player.sendMessage(ConfigManager.getMessage("level-removed").replace("{amount}", amount.toString()))
                    }
                }
            }

            "level" -> {
                val uuid = player.uniqueId
                val xp = PlayerDataManager.getPlayerXP(uuid)
                val level = PlayerDataManager.getPlayerLevel(uuid)
                player.sendMessage(
                    ConfigManager.getMessage("level-info")
                        .replace("{level}", level.toString())
                        .replace("{xp}", xp.toString())
                )
            }

            "help" -> {
                player.sendMessage(ConfigManager.getMessage("help-header"))
                player.sendMessage(ConfigManager.getMessage("help-addxp"))
                player.sendMessage(ConfigManager.getMessage("help-takexp"))
                player.sendMessage(ConfigManager.getMessage("help-addlevel"))
                player.sendMessage(ConfigManager.getMessage("help-setlevel"))
                player.sendMessage(ConfigManager.getMessage("help-takelevel"))
                player.sendMessage(ConfigManager.getMessage("help-level"))
                player.sendMessage(ConfigManager.getMessage("help-help"))
            }

            else -> {
                player.sendMessage(ConfigManager.getMessage("unknown-subcommand"))
            }
        }

        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String> {
        if (args.isEmpty()) {
            return listOf("addxp", "takexp", "addlevel", "takelevel", "level", "help")
        }

        val subCommands = listOf("addxp", "takexp", "addlevel", "takelevel", "level", "help")

        return when (args.size) {
            1 -> StringUtil.copyPartialMatches(args[0], subCommands, mutableListOf())
            2 -> if (args[0].lowercase() in listOf("addxp", "takexp", "addlevel", "takelevel")) {
                Bukkit.getOnlinePlayers().map { it.name }
            } else emptyList()
            3 -> if (args[0].lowercase() in listOf("addxp", "takexp", "addlevel", "takelevel")) {
                listOf("<amount>").filter { it.startsWith(args[2]) }
            } else emptyList()
            else -> emptyList()
        }
    }
}
