package me.barsam.playerLevels.managers

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.UUID
import me.barsam.playerLevels.PlayerLevels

object RewardManager {
    private var economy: Economy? = null

    fun initialize(economy: Economy?) {
        this.economy = economy
    }

    fun giveRewards(uuid: UUID, level: Int) {
        val config = PlayerLevels.instance
        val cachedConfig = config.getCachedConfigValue("rewards.$level") as? Map<*, *> ?: return

        val player: Player = Bukkit.getPlayer(uuid) ?: return

        // Give item
        val itemConfig = cachedConfig["item"] as? Map<*, *> ?: return
        if (itemConfig.isNotEmpty()) {
            val material = Material.matchMaterial(itemConfig["material"] as? String ?: "STONE") ?: return
            val itemStack = ItemStack(material, (itemConfig["amount"] as? Int) ?: 1)
            val meta: ItemMeta = itemStack.itemMeta ?: return

            meta.setDisplayName((itemConfig["name"] as? String)?.replace("&", "§"))
            meta.lore = (itemConfig["lore"] as? List<String>)?.map { it.replace("&", "§") } ?: emptyList()

            itemStack.itemMeta = meta
            player.inventory.addItem(itemStack)
        }

        // Run command
        val command = cachedConfig["command"] as? String
        command?.let {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), it.replace("%player_name%", player.name))
        }

        // Give Vault money
        val vaultAmount = cachedConfig["vault"] as? Double
        if (vaultAmount != null) {
            economy?.depositPlayer(player, vaultAmount)
        }

        val rewardMessage = config.getCachedConfigValue("messages.reward_received") as? String
        rewardMessage?.let {
            player.sendMessage(it.replace("%level%", level.toString()))
        }
    }
}
