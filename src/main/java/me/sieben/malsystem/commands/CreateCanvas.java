/**
 *
 * TODO
 * - usage
 * - tab complete
 */


package me.sieben.malsystem.commands;

import me.sieben.malsystem.MalSystem;
import me.sieben.malsystem.utils.Canvas;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CreateCanvas implements CommandExecutor , TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(MalSystem.pluginPrefix + "You are not a Player");
            return false;
        }

        Player player = (Player) sender;

        /* TODO
        if (!(player.hasPermission(new MalSystem().getConfig().get("permission.create_canvas").toString())))
        {
            player.sendMessage("You don't have the permission to perform that command");
            return false;
        }
        */


        if (args.length == 0)
        {
            player.sendMessage(MalSystem.pluginPrefix + "/help");
            return false;
        }

        switch (args[0])
        {

            case ("create"):
            {

                if (args.length != 10) {
                    player.sendMessage(MalSystem.pluginPrefix + "/usage");
                    return false;
                }

                String canvasName = args[1];

                int posX1 = Integer.parseInt(args[2]);
                int posY1 = Integer.parseInt(args[3]);
                int posZ1 = Integer.parseInt(args[4]);

                int posX2 = Integer.parseInt(args[5]);
                int posY2 = Integer.parseInt(args[6]);
                int posZ2 = Integer.parseInt(args[7]);

                int width = Integer.parseInt(args[8]);
                int height = Integer.parseInt(args[9]);

                FileConfiguration config = MalSystem.getInstance().canvasConfig;
                File configFile = MalSystem.getInstance().canvasConfigFile;

                List<String> names = config.getStringList("canvas.canvas_names");
                names.add(canvasName);

                config.set("canvas.canvas_names", names);

                config.set("canvas." + canvasName + ".x_pos_1", posX1);
                config.set("canvas." + canvasName + ".y_pos_1", posY1);
                config.set("canvas." + canvasName + ".z_pos_1", posZ1);

                config.set("canvas." + canvasName + ".x_pos_2", posX2);
                config.set("canvas." + canvasName + ".y_pos_2", posY2);
                config.set("canvas." + canvasName + ".z_pos_2", posZ2);

                config.set("canvas." + canvasName + ".x_tp", player.getLocation().getX());
                config.set("canvas." + canvasName + ".y_tp", player.getLocation().getY());
                config.set("canvas." + canvasName + ".z_tp", player.getLocation().getZ());

                config.set("canvas." + canvasName + ".width", width);
                config.set("canvas." + canvasName + ".height", height);

                try {
                    config.save(configFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (MalSystem.getInstance().loadCanvases()) {
                    player.sendMessage(MalSystem.pluginPrefix + "Reloaded all Canvases!");
                }else player.sendMessage(MalSystem.pluginPrefix + "Error loading Canvases");

                break;
            }


            case ("delete"):
            {
                if (!(args.length == 2)) {
                    player.sendMessage(MalSystem.pluginPrefix + "/usage");
                    return false;
                }

                String canvasName = args[1];

                FileConfiguration config = MalSystem.getInstance().canvasConfig;
                File configFile = MalSystem.getInstance().canvasConfigFile;

                List<String> names = config.getStringList("canvas.canvas_names");
                names.remove(canvasName);

                config.set("canvas.canvas_names", names);

                config.set("canvas." + canvasName, "");

                try {
                    config.save(configFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                player.sendMessage(MalSystem.pluginPrefix + "Deleted Canvas " + canvasName);

                break;
            }



            case ("reload"):
            {
                if (MalSystem.getInstance().loadCanvases()) {
                    player.sendMessage(MalSystem.pluginPrefix + "Reloaded all Canvases!");
                }else player.sendMessage(MalSystem.pluginPrefix + "Error loading Canvases");
                break;
            }


            default:
            {

                player.sendMessage(MalSystem.pluginPrefix + "/help");

            }

        }


        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return null; // Tab completion only works for players
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            // Tab complete the first argument (subcommands)
            List<String> subcommands = Arrays.asList("create", "delete", "reload");
            return filterStartingWith(args[0], subcommands);
        } else if (args.length >= 2 && args.length <= 10) {
            // Tab complete the arguments based on the first argument
            switch (args[0]) {
                case "create":
                    return tabCompleteCreateArguments(args, player);
                case "delete":
                    if (args.length == 2) return filterStartingWith(args[1], getCanvasNames());
            }
        }

        return null; // Default to no tab completion
    }

    private List<String> filterStartingWith(String prefix, List<String> options) {
        return options.stream()
                .filter(option -> option.startsWith(prefix))
                .collect(Collectors.toList());
    }

    private List<String> tabCompleteCreateArguments(String[] args, Player player) {
        if (args.length == 2) {
            // Tab complete the canvas name
            return filterStartingWith(args[1], getCanvasNames());
        } else if (args.length >= 3 && args.length <= 8) {
            // Tab complete the position arguments with the block positions the player is looking at
            Location targetBlockLocation = player.getTargetBlock(null, 5).getLocation();
            int targetBlockX = targetBlockLocation.getBlockX();
            int targetBlockY = targetBlockLocation.getBlockY();
            int targetBlockZ = targetBlockLocation.getBlockZ();

            switch (args.length) {
                case 3:
                case 6:
                    return Arrays.asList(Integer.toString(targetBlockX));
                case 4:
                case 7:
                    return Arrays.asList(Integer.toString(targetBlockY));
                case 5:
                case 8:
                    return Arrays.asList(Integer.toString(targetBlockZ));
            }
        }

        return null;
    }

    private List<String> getCanvasNames() {
        // Replace with your logic to retrieve canvas names from your configuration
        // For now, returning an empty list as a placeholder
        return Canvas.canvasNames;
    }

}
