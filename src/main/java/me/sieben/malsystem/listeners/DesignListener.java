package me.sieben.malsystem.listeners;

import me.sieben.malsystem.commands.DesignCommand;
import me.sieben.malsystem.utils.BlockUtils;
import me.sieben.malsystem.utils.Canvas;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;


public class DesignListener implements Listener {


    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() == null) return;
        if (!(DesignCommand.assignedPlayers.containsKey(player))) return;


        if (event.getMaterial() == Material.INK_SACK) {

            if (event.getClickedBlock() == null) return;
            if (event.getClickedBlock().getType() != Material.WOOL) return;

            byte color = event.getItem().getData().getData();

            event.getClickedBlock().setData(DyeColor.getByDyeData(color).getWoolData());

        } else if (event.getMaterial() == Material.BARRIER) {
            //TODO Confirm Reset
            event.setCancelled(true);
            Canvas canvas = DesignCommand.assignedPlayers.get(player);

            int[] posStart = canvas.getCanvasPosStart();
            int[] posEnd = canvas.getCanvasPosEnd();

            List<Block> blockList = BlockUtils.generateBlocks(
                    player.getWorld(),
                    posStart[0],
                    posStart[1],
                    posStart[2],
                    posEnd[0],
                    posEnd[1],
                    posEnd[2]);

            for (Block b : blockList) {
                b.setData(DyeColor.WHITE.getWoolData());
            }

        }
    }
}
