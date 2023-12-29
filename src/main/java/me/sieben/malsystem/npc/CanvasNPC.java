package me.sieben.malsystem.npc;


import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.MetadataValue;

public class CanvasNPC {


    public void spawnNpc(Location loc) {

        Entity npc = loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);

        npc.setCustomNameVisible(false);
        npc.setCustomName("NPC-DESIGN");


    }
}
