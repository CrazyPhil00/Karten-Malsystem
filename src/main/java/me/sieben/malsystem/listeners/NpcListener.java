package me.sieben.malsystem.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class NpcListener implements Listener {

    @EventHandler
    public void onSpawn(EntityInteractEvent event) {

        if (event.getEntity().getCustomName().equalsIgnoreCase("NPC-DESIGN")) {


        }
    }
}
