/**
 * TODO
 *
 * - Confirm Reset
 *
 */




package me.sieben.malsystem.listeners;

import me.sieben.malsystem.MalSystem;
import me.sieben.malsystem.commands.DesignCommand;
import me.sieben.malsystem.renderer.CanvasRenderer;
import me.sieben.malsystem.utils.BlockUtils;
import me.sieben.malsystem.utils.Canvas;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;
import java.util.List;

public class DesignListener implements Listener {


    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = null;

        if (event.getItem() == null) return;
        if (!(DesignCommand.assignedPlayers.containsKey(player))) return;


        if (event.getMaterial() == Material.INK_SACK) {

            if (event.getClickedBlock() == null) block = player.getTargetBlock(null, 50);
            else if (event.getClickedBlock().getType() == Material.WOOL) block = event.getClickedBlock();

            byte color = event.getItem().getData().getData();

            block.setData(DyeColor.getByDyeData(color).getWoolData());

        } else if (event.getMaterial() == Material.BARRIER) {
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


