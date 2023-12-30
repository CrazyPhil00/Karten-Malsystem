package me.sieben.malsystem.npc;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.metadata.MetadataValue;

public class CanvasNPC {


    public void spawnNpc(Location loc, Player player) {

        Villager npc = (Villager) Bukkit.getWorld("").spawnEntity(player.getLocation(), EntityType.VILLAGER);




        npc.setCustomNameVisible(false);
        npc.setCustomName("NPC-DESIGN");


    }
}
