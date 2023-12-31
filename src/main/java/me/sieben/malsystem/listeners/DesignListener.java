package me.sieben.malsystem.listeners;

import me.sieben.malsystem.MalSystem;
import me.sieben.malsystem.utils.BlockUtils;
import me.sieben.malsystem.utils.Canvas;
import me.sieben.malsystem.utils.CanvasUtils;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.List;

public class DesignListener implements Listener {

    private final ArrayList<Player> confirmReset = new ArrayList<>();

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = null;

        if (event.getItem() == null) return;
        if (!(Canvas.containsAssignedPlayer(player))) return;


        if (event.getMaterial() == Material.INK_SACK) {

            if (event.getClickedBlock() == null) block = player.getTargetBlock(null, 50);
            else if (event.getClickedBlock().getType() == Material.WOOL) block = event.getClickedBlock();

            if (block == null) return;

            byte color = event.getItem().getData().getData();

            block.setData(DyeColor.getByDyeData(color).getWoolData());

        } else if (event.getMaterial() == Material.BARRIER) {
            if (confirmReset.contains(player)) {
                event.setCancelled(true);

                resetCanvas(player);
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

            CanvasUtils.loadInv(player);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if (!(Canvas.containsAssignedPlayer(player))) return;

        player.sendMessage(MalSystem.pluginPrefix + "You left the Canvas Area, your Map was not saved.");

        Canvas.removeAssignedPlayer(player);

        CanvasUtils.loadInv(player);

        resetCanvas(player);

        Canvas.oldPlayerPos.remove(player);
    }

    @SuppressWarnings("deprecation")
    public static void resetCanvas(Player player) {
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
    }
}


