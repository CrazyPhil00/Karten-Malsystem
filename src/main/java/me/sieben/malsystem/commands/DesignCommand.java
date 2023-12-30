/**
 *
 * TODO BUG:
 * - Player Leaving Server
 *
 */




package me.sieben.malsystem.commands;

import de.infinitycity.banksystem.apis.buy.BuyAPI;
import me.sieben.malsystem.MalSystem;
import me.sieben.malsystem.renderer.CanvasRenderer;
import me.sieben.malsystem.utils.BlockUtils;
import me.sieben.malsystem.utils.Canvas;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class DesignCommand implements CommandExecutor {


    private static HashMap<Player, ItemStack[]> savedInv = new HashMap<>();
    private ArrayList<Player> confirmAbort = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(MalSystem.pluginPrefix + "You cannot use this command!");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(MalSystem.pluginPrefix + "/design help");
            return false;
        }

        switch (args[0])
        {

            case ("help"):
            {

                break;
            }

            case ("create"):
            {

                if (args.length < 3) {
                    player.sendMessage(MalSystem.pluginPrefix + "Enter a width and a size!");
                    return false;
                }

                 int width = Integer.parseInt(args[1]);
                 int height = Integer.parseInt(args[2]);

                 if (Canvas.createCanvas(player, width, height)) {
                     player.sendMessage(MalSystem.pluginPrefix + "Created Canvas");
                 }else {
                     player.sendMessage(MalSystem.pluginPrefix + "Can't create canvas.");
                 }



                break;
            }

            case ("save"):
            {
                if (!(savedInv.containsKey(player)) &! Canvas.containsAssignedPlayer(player)) {
                    player.sendMessage(MalSystem.pluginPrefix + "Can't save canvas.");
                    return false;
                }


                saveMap(player);



                break;
            }

            case ("exit"):
            {

                if (!(Canvas.containsAssignedPlayer(player))) {
                    player.sendMessage(MalSystem.pluginPrefix + "You are currently not drawing");
                    return false;
                }

                if (confirmAbort.contains(player)) {
                    exitMap(player);
                    confirmAbort.remove(player);
                }else {

                    player.sendMessage(MalSystem.pluginPrefix + "Do you really want to exit? The Image will not be saved!");

                    TextComponent component = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "[Exit]")));
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/design exit"));

                    player.spigot().sendMessage(component);
                    confirmAbort.add(player);

                }

            }
        }



        return false;
    }


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

        loadInv(player);

        Canvas.assignedPlayers.get(player).setInUse(false);
        Canvas.assignedPlayers.remove(player);
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

        BuyAPI.buySingleItem(player, map, 50, new BuyAPI.BuyCallback() {
            @Override
            public void success(Player player) {
                player.sendMessage(MalSystem.pluginPrefix + "Success");

                loadInv(player);
                player.getInventory().addItem(map);

                Canvas.getAssignedPlayer(player).setInUse(false);
                Canvas.removedAssignedPlayer(player);
            }

            @Override
            public void abort(Player player) {
                player.sendMessage(MalSystem.pluginPrefix + "Abort");
            }
        });

    }
}
