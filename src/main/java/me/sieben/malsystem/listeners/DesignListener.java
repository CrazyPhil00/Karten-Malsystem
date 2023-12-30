/**
 * TODO
 *
 * - Confirm Reset
 *
 */



package me.sieben.malsystem.listeners;

import me.sieben.malsystem.MalSystem;
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
import org.bukkit.event.player.PlayerQuitEvent;


import java.util.ArrayList;
import java.util.List;

public class DesignListener implements Listener {

    private ArrayList<Player> confirmReset = new ArrayList<>();

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = null;

        if (event.getItem() == null) return;
        if (!(Canvas.containsAssignedPlayer(player))) return;


        if (event.getMaterial() == Material.INK_SACK) {

            if (event.getClickedBlock() == null) block = player.getTargetBlock(null, 50);
            else if (event.getClickedBlock().getType() == Material.WOOL) block = event.getClickedBlock();

            byte color = event.getItem().getData().getData();

            block.setData(DyeColor.getByDyeData(color).getWoolData());

        } else if (event.getMaterial() == Material.BARRIER) {
            if (confirmReset.contains(player)) {
                event.setCancelled(true);
                Canvas canvas = Canvas.getAssignedPlayer(player);

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
                player.sendMessage(MalSystem.pluginPrefix + "Canvas has been reset.");

                confirmReset.remove(player);
            }else {
                player.sendMessage(MalSystem.pluginPrefix + "Do you relly want to §c§lreset?");
                player.sendMessage(MalSystem.pluginPrefix + "Press again to §lconfirm.");
                confirmReset.add(player);
            }

        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (Canvas.containsAssignedPlayer(player)) {
            Canvas.removeAssignedPlayer(player);

            DesignCommand.loadInv(player);
        }
    }
}


