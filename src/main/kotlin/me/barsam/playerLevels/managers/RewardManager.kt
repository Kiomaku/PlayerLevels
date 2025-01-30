package me.barsam.playerLevels.managers

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.UUID

object RewardManager {
    private lateinit var config: FileConfiguration
    private var economy: Economy? = null

    fun initialize(config: FileConfiguration, economy: Economy?) {
        this.config = config
        this.economy = economy
    }

    fun giveRewards(uuid: UUID, level: Int) {
        if (!this::config.isInitialized) {
            Bukkit.getLogger().severe("[PlayerLevels] ERROR: RewardManager config is not initialized!")
            return
        }

        val player: Player = Bukkit.getPlayer(uuid) ?: return
        val rewardsSection = config.getConfigurationSection("rewards.$level") ?: return

        // Give item
        if (rewardsSection.contains("item")) {
            val itemConfig = rewardsSection.getConfigurationSection("item")!!
            val material = Material.matchMaterial(itemConfig.getString("material", "STONE")!!) ?: return
            val itemStack = ItemStack(material, itemConfig.getInt("amount", 1))
            val meta: ItemMeta = itemStack.itemMeta ?: return

            meta.setDisplayName(itemConfig.getString("name")?.replace("&", "§"))
            meta.lore = itemConfig.getStringList("lore").map { it.replace("&", "§") }

            itemStack.itemMeta = meta
            player.inventory.addItem(itemStack)
        }

        // Run command
        if (rewardsSection.contains("command")) {
            val command = rewardsSection.getString("command", "")?.replace("%player_name%", player.name)
            if (!command.isNullOrEmpty()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
            }
        }

        // Give Vault money
        if (rewardsSection.contains("vault")) {
            val amount = rewardsSection.getDouble("vault")
            economy?.depositPlayer(player, amount)
        }

        config.getString("messages.reward_received", "")
            ?.let {
                player.sendMessage(it.replace("%level%", level.toString()))
            }
    }
}
