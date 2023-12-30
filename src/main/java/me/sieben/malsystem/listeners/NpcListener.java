package me.sieben.malsystem.listeners;

import me.sieben.malsystem.commands.DesignCommand;
import me.sieben.malsystem.commands.NPCCommand;
import me.sieben.malsystem.gui.NPCGui;
import me.sieben.malsystem.utils.Canvas;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import sun.security.krb5.internal.crypto.Des;

public class NpcListener implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.VILLAGER) {
            Player player = event.getPlayer();
            Entity npc = event.getRightClicked();

            if (npc.getCustomName() == null) return;



            if (NPCCommand.getNPCMetadata(npc, "npc-type").equals("CREATE-CANVAS")) {
                event.setCancelled(true);
                new NPCGui().openGuiCreateMap(player);
            }

            else if (NPCCommand.getNPCMetadata(npc, "npc-type").equals("SAVE-CANVAS")) {
                event.setCancelled(true);
                new NPCGui().openGuiSaveMap(player);
            }

        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;
        Player player = (Player) event.getWhoClicked();

        ItemStack itemStack = event.getCurrentItem();

        if (event.getClickedInventory().getName().equalsIgnoreCase("      §8§kk§r §2§lSelect Map Size §8§kk")) {
            event.setCancelled(true);
            int size = Integer.parseInt(itemStack.getItemMeta().getLocalizedName());

            Canvas.createCanvas(player, size, size);

        }else if (event.getClickedInventory().getName().equalsIgnoreCase("      §8§kk§r §2§lBuy §7or §c§lExit §8§kk")) {
            event.setCancelled(true);

            if (itemStack.getType() == Material.EMERALD) {

                DesignCommand.saveMap(player);

            }else if (itemStack.getType() == Material.BARRIER) {
                new NPCGui().confirmExit(player);
            }

        }else if (event.getClickedInventory().getName().equalsIgnoreCase("      §8§kk§r §c§lConfirm Exit §8§kk")) {
            event.setCancelled(true);
            DesignCommand.exitMap(player);
            player.closeInventory();
        }

        if (Canvas.containsAssignedPlayer(player)) {
            if (event.getClickedInventory().getName().equalsIgnoreCase(player.getInventory().getName())) {
                event.setCancelled(true);
            }
        }

    }
}
