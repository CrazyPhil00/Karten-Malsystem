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

    private HashMap<Player, ItemStack[]> savedInv = new HashMap<>();
    public static HashMap<Player, Canvas> assignedPlayers = new HashMap<>();

    private ArrayList<Player> confirmAbort = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("You cannot use this command!");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("/design help");
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
                    player.sendMessage("Enter a width and a size!");
                    return false;
                }

                 int width = Integer.parseInt(args[1]);
                 int height = Integer.parseInt(args[2]);

                 if (createCanvas(player, width, height)) {

                     saveInv(player);
                     giveColors(player);

                 }else {
                     player.sendMessage("Can't create canvas.");
                 }



                break;
            }

            case ("save"):
            {
                if (!(savedInv.containsKey(player)) &! assignedPlayers.containsKey(player)) {
                    player.sendMessage("Can't save canvas.");
                    return false;
                }

                Canvas canvas = assignedPlayers.get(player);

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

                createMap(player, canvas.getWidth());



                break;
            }

            case ("exit"):
            {

                if (confirmAbort.contains(player)) {
                    loadInv(player);

                    assignedPlayers.get(player).setInUse(false);
                    assignedPlayers.remove(player);
                    confirmAbort.remove(player);
                }else {

                    player.sendMessage("Do you really want to exit? The Image will not be saved!");

                    TextComponent component = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "[Exit]")));
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/design exit"));

                    player.spigot().sendMessage(component);
                    confirmAbort.add(player);

                }

            }
        }



        return false;
    }


    private boolean createCanvas(Player player, int width, int height) {
        if (assignedPlayers.containsKey(player)) {
            player.sendMessage("You are already using a canvas.");
            return false;
        }
        Canvas canvas = Canvas.getEmpty(MalSystem.canvasList, width, height);

        if (canvas == null) {
            player.sendMessage("Error while Creating Canvas.");
            return false;
        }

        assignedPlayers.put(player, canvas);

        player.teleport(canvas.getTpPos());

        return true;
    }

    private void saveInv(Player player) {
        savedInv.put(player, player.getInventory().getContents());
        player.getInventory().clear();
    }

    private void loadInv(Player player) {
        player.getInventory().clear();
        player.getInventory().setContents(savedInv.get(player));
        savedInv.remove(player);

    }

    private void giveColors(Player player) {

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

    private void createMap(Player player, int size) {
        MapView view = Bukkit.createMap(player.getWorld());


        for (MapRenderer renderer : view.getRenderers()) {
            view.removeRenderer(renderer);
        }
        int uID = view.getId();
        CanvasRenderer mapRenderer = new CanvasRenderer();

        List<BlockUtils> list = MalSystem.relativeBlockList.get(player);
        BlockUtils.saveImage(player, list, uID);

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
                player.sendMessage("Success");

                loadInv(player);
                player.getInventory().addItem(map);

                assignedPlayers.get(player).setInUse(false);
                assignedPlayers.remove(player);
            }

            @Override
            public void abort(Player player) {
                player.sendMessage("Abort");
            }
        });

    }
}
