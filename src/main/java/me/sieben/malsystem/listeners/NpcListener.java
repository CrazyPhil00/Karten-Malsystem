package me.sieben.malsystem.listeners;

import me.sieben.malsystem.utils.CanvasUtils;
import me.sieben.malsystem.gui.NPCGui;
import me.sieben.malsystem.utils.Canvas;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class NpcListener implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.VILLAGER) {
            Player player = event.getPlayer();
            Entity npc = event.getRightClicked();

            if (!(NPCGui.npcConfig.isSet(npc.getUniqueId().toString()))) return;

            if (NPCGui.npcConfig.getString(npc.getUniqueId().toString()).equalsIgnoreCase("CREATE-CANVAS")) {
                event.setCancelled(true);
                new NPCGui().openGuiCreateMap(player);
            } else if (NPCGui.npcConfig.getString(npc.getUniqueId().toString()).equalsIgnoreCase("SAVE-CANVAS")) {
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

        } else if (event.getClickedInventory().getName().equalsIgnoreCase("         §8§kk§r §2§lBuy §7or §c§lExit §8§kk")) {
            event.setCancelled(true);

            if (itemStack.getType() == Material.EMERALD) {

                CanvasUtils.saveMap(player);

            } else if (itemStack.getType() == Material.BARRIER) {
                new NPCGui().confirmExit(player);
            }

        } else if (event.getClickedInventory().getName().equalsIgnoreCase("         §8§kk§r §c§lConfirm Exit §8§kk")) {
            event.setCancelled(true);
            CanvasUtils.exitMap(player);
            player.closeInventory();
        }

        if (Canvas.containsAssignedPlayer(player)) {
            if (event.getClickedInventory().getName().equalsIgnoreCase(player.getInventory().getName())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (Canvas.containsAssignedPlayer(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onNPCDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (!(entity.getType() == EntityType.VILLAGER)) return;

        if (entity.isInvulnerable()) event.setCancelled(true);
    }

}
