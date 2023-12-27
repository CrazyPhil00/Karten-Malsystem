package me.sieben.malsystem.listeners;

import me.sieben.malsystem.commands.DesignCommand;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;


public class DesignListener implements Listener {


    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() == null) return;

        if (event.getClickedBlock() == null) return;

        if (event.getClickedBlock().getType() != Material.WOOL) return;

        if (!(DesignCommand.assignedPlayers.containsKey(player))) return;




        Block block = event.getClickedBlock();

        if (event.getMaterial() == Material.INK_SACK) {

            byte color = event.getItem().getData().getData();

            block.setData(DyeColor.getByDyeData(color).getWoolData());

        }
    }
}
