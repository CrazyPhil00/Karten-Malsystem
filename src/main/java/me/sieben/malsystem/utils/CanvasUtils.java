

package me.sieben.malsystem.utils;

import de.infinitycity.banksystem.apis.buy.BuyAPI;
import me.sieben.malsystem.MalSystem;
import me.sieben.malsystem.listeners.DesignListener;
import me.sieben.malsystem.renderer.CanvasRenderer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings("deprecation")
public class CanvasUtils {


    private static final HashMap<Player, ItemStack[]> savedInv = new HashMap<>();



    public static void saveInv(Player player) {
        savedInv.put(player, player.getInventory().getContents());
        player.getInventory().clear();
    }

    public static void loadInv(Player player) {
        player.getInventory().clear();
        player.getInventory().setContents(savedInv.get(player));
        savedInv.remove(player);

    }

    public static void giveColors(Player player) {

        ItemStack red = new ItemStack(Material.INK_SACK);
        red.setDurability(DyeColor.RED.getDyeData());

        ItemStack orange = new ItemStack(Material.INK_SACK);
        orange.setDurability(DyeColor.ORANGE.getDyeData());

        ItemStack yellow = new ItemStack(Material.INK_SACK);
        yellow.setDurability(DyeColor.YELLOW.getDyeData());

        ItemStack green = new ItemStack(Material.INK_SACK);
        green.setDurability(DyeColor.GREEN.getDyeData());

        ItemStack light_blue = new ItemStack(Material.INK_SACK);
        light_blue.setDurability(DyeColor.LIGHT_BLUE.getDyeData());

        ItemStack blue = new ItemStack(Material.INK_SACK);
        blue.setDurability(DyeColor.BLUE.getDyeData());

        ItemStack brown = new ItemStack(Material.INK_SACK);
        brown.setDurability(DyeColor.BROWN.getDyeData());

        ItemStack white = new ItemStack(Material.INK_SACK);
        white.setDurability(DyeColor.WHITE.getDyeData());

        ItemStack barrier = new ItemStack(Material.BARRIER);
        barrier.setDurability(DyeColor.WHITE.getDyeData());

        player.getInventory().setItem(0, red);
        player.getInventory().setItem(1, orange);
        player.getInventory().setItem(2, yellow);
        player.getInventory().setItem(3, green);
        player.getInventory().setItem(4, light_blue);
        player.getInventory().setItem(5, blue);
        player.getInventory().setItem(6, brown);
        player.getInventory().setItem(7, white);
        player.getInventory().setItem(8, barrier);

    }

    public static void exitMap(Player player) {
        if (!(Canvas.containsAssignedPlayer(player))) {
            player.sendMessage(MalSystem.pluginPrefix + "Error while getting Canvas");
            return;
        }

        player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1, 1);

        loadInv(player);

        DesignListener.resetCanvas(player);

        Canvas.removeAssignedPlayer(player);

        if (Canvas.oldPlayerPos.containsKey(player)) {
            player.teleport(Canvas.oldPlayerPos.get(player));
            Canvas.oldPlayerPos.remove(player);
        }
    }

    public static void saveMap(Player player) {

        Canvas canvas = Canvas.getAssignedPlayer(player);

        if (canvas == null) {
            player.sendMessage(MalSystem.pluginPrefix + "Error while getting Canvas");
            return;
        }

        int[] posStart = canvas.getCanvasPosStart();
        int[] posEnd = canvas.getCanvasPosEnd();

        MalSystem.relativeBlockList.put(
                player, BlockUtils.convertTo2DList(
                        BlockUtils.generateBlocks(
                                player.getWorld(),
                                posStart[0],
                                posStart[1],
                                posStart[2],
                                posEnd[0],
                                posEnd[1],
                                posEnd[2]),
                        canvas.getHeight()));

        int size = canvas.getWidth();


        MapView view = Bukkit.createMap(player.getWorld());


        for (MapRenderer renderer : view.getRenderers()) {
            view.removeRenderer(renderer);
        }
        int uID = view.getId();
        CanvasRenderer mapRenderer = new CanvasRenderer();

        List<BlockUtils> list = MalSystem.relativeBlockList.get(player);
        BlockUtils.saveImage(list, uID);

        mapRenderer.loadImage(BlockUtils.convertToImage(list, size));


        view.addRenderer(mapRenderer);

        ItemStack map = new ItemStack(Material.MAP, 1, view.getId());
        MapMeta meta = (MapMeta) map.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GRAY + "Map of " + ChatColor.BLUE + "" + ChatColor.BOLD + player.getName());
        meta.setLocalizedName(String.valueOf(uID));
        map.setItemMeta(meta);

        String config_path = "npc.select-canvas.item-slot.";

        float price = 50;

        for (int i = 0; i < 26; i++) {
            if (MalSystem.getInstance().getConfig().isSet(config_path + i)) {
                if (canvas.getWidth() == MalSystem.getInstance().getConfig().getInt(config_path + i + ".canvas-size")) {
                    price = MalSystem.getInstance().getConfig().getInt(config_path + i + ".price");
                }
            }
        }


        BuyAPI.buySingleItem(player, map, price, new BuyAPI.BuyCallback() {
            @Override
            public void success(Player player) {
                player.sendMessage(MalSystem.pluginPrefix + "Successfully bought Map.");
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 50, 1);

                loadInv(player);
                player.getInventory().addItem(map);

                DesignListener.resetCanvas(player);

                Canvas.removeAssignedPlayer(player);

                if (Canvas.oldPlayerPos.containsKey(player)) {
                    player.teleport(Canvas.oldPlayerPos.get(player));
                    Canvas.oldPlayerPos.remove(player);
                }
            }

            @Override
            public void abort(Player player) {
                player.sendMessage(MalSystem.pluginPrefix + "Abort");
            }
        });

    }
}
