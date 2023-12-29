package me.sieben.malsystem.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class NPCGui {

    public void createMap(Player player) {

        Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST, ChatColor.DARK_PURPLE + "Create Map");




    }
}
