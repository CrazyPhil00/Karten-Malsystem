package me.sieben.malsystem.commands;

import me.sieben.malsystem.MalSystem;
import me.sieben.malsystem.utils.Canvas;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class DesignCommand implements CommandExecutor {

    private HashMap<Player, ItemStack[]> savedInv = new HashMap<>();
    public static HashMap<Player, Canvas> assignedPlayers = new HashMap<>();

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

        switch (args[0]) {

            case ("help"):
            {

                break;
            }

            case ("create"):
            {

                /**
                if (args.length < 3) {
                    player.sendMessage("Enter a width and a size!");
                    return false;
                }

                 int width = Integer.parseInt(args[1]);
                 int heigth = Integer.parseInt(args[2]);
                 **/

                saveInv(player);
                giveColors(player);

                createCanvas(player, 0, 0);

                break;
            }

            case ("save"):
            {
                loadInv(player);
                break;
            }
        }



        return false;
    }


    private void createCanvas(Player player, int width, int height) {

        Canvas canvas = Canvas.getEmpty(MalSystem.canvasList);

        if (canvas == null) {
            System.out.println("Canvas is NULL");
            return;
        }

        assignedPlayers.put(player, canvas);

        player.teleport(canvas.getTpPos());


    }

    private void saveInv(Player player) {
        savedInv.put(player, player.getInventory().getContents());
        player.getInventory().clear();
    }

    private void loadInv(Player player) {

        if (!(savedInv.containsKey(player))) return;

        player.getInventory().setContents(savedInv.get(player));
        player.getInventory().clear();

        savedInv.remove(player);
        assignedPlayers.remove(player);

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

        player.getInventory().setItem(0, red);
        player.getInventory().setItem(1, orange);
        player.getInventory().setItem(2, yellow);
        player.getInventory().setItem(3, green);
        player.getInventory().setItem(4, light_blue);
        player.getInventory().setItem(5, blue);
        player.getInventory().setItem(6, brown);
        player.getInventory().setItem(7, white);

    }
}
