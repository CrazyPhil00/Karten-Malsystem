package me.sieben.malsystem.commands;

import me.sieben.malsystem.MalSystem;
import me.sieben.malsystem.utils.Canvas;
import me.sieben.malsystem.utils.CanvasUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CreateCanvas implements CommandExecutor , TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(MalSystem.pluginPrefix + "Only Players can perform this Command!");
            return false;
        }

        Player player = (Player) sender;


        if (!(player.hasPermission(MalSystem.getInstance().getConfig().getString("permission.modify-canvas"))))
        {
            player.sendMessage(MalSystem.pluginPrefix + "You don't have the permission to perform that command");
            return false;
        }

        if (args.length == 0)
        {
            player.sendMessage(getHelp());
            return false;
        }

        switch (args[0])
        {

            case ("create"):
            {

                if (args.length != 10) {
                    player.sendMessage(getHelp());
                    return false;
                }

                String canvasName = args[1];

                int posX1, posY1, posZ1, posX2, posY2, posZ2, width, height;

                try {
                    posX1 = Integer.parseInt(args[2]);
                    posY1 = Integer.parseInt(args[3]);
                    posZ1 = Integer.parseInt(args[4]);

                    posX2 = Integer.parseInt(args[5]);
                    posY2 = Integer.parseInt(args[6]);
                    posZ2 = Integer.parseInt(args[7]);

                    width = Integer.parseInt(args[8]);
                    height = Integer.parseInt(args[9]);
                }catch (NumberFormatException e) {
                    player.sendMessage(MalSystem.pluginPrefix + "Please enter valid values.");
                    return false;
                }



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

                config.set("canvas." + canvasName + ".tp", player.getLocation());


                config.set("canvas." + canvasName + ".width", width);
                config.set("canvas." + canvasName + ".height", height);

                config.set("canvas." + canvasName + ".direction", player.getLocation().getDirection());

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


            case ("remove"):
            {
                if (args.length != 2) {
                    player.sendMessage(getHelp());
                    return false;
                }

                String canvasName = args[1];

                FileConfiguration config = MalSystem.getInstance().canvasConfig;
                File configFile = MalSystem.getInstance().canvasConfigFile;

                List<String> names = config.getStringList("canvas.canvas_names");

                if (!names.contains(canvasName)) {
                    player.sendMessage(MalSystem.pluginPrefix + canvasName + " doesn't exist!");
                    return false;
                }

                names.remove(canvasName);

                config.set("canvas.canvas_names", names);
                config.set("canvas." + canvasName, "");

                try {
                    config.save(configFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                player.sendMessage(MalSystem.pluginPrefix + "Deleted Canvas " + canvasName);

                if (MalSystem.getInstance().loadCanvases()) {
                    player.sendMessage(MalSystem.pluginPrefix + "Reloaded all Canvases!");
                }else player.sendMessage(MalSystem.pluginPrefix + "Error loading Canvases");

                break;
            }



            case ("reload"):
            {
                if (MalSystem.getInstance().loadCanvases()) {
                    player.sendMessage(MalSystem.pluginPrefix + "Reloaded all Canvases!");
                }else player.sendMessage(MalSystem.pluginPrefix + "Error loading Canvases");
                break;
            }

            case ("stats"):
            {
                player.sendMessage(MalSystem.pluginPrefix + CanvasUtils.vector2Direction(player.getLocation().getDirection()));
                break;
            }


            default:
            {

                player.sendMessage(getHelp());

            }

        }


        return false;
    }

    private String getHelp() {
        return
                MalSystem.pluginPrefix +         "§8§l------------§9Help§8§l-----------\n" +
                        MalSystem.pluginPrefix + "/canvas create name x1 y1 z1 x2 y2 z2 width height:\n" +
                        MalSystem.pluginPrefix + "Creates a new Canvas with the entered values.\n" +
                        MalSystem.pluginPrefix + "§8--------------------------------------\n" +
                        MalSystem.pluginPrefix + "/canvas remove name: \n\n" +
                        MalSystem.pluginPrefix + "Deletes the selected Canvas\n";
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            List<String> subcommands = Arrays.asList("create", "delete", "reload", "help");
            return filterStartingWith(args[0], subcommands);
        } else if (args.length >= 2 && args.length <= 10) {
            switch (args[0]) {
                case "create":
                    return tabCompleteCreateArguments(args, player);
                case "delete":
                    if (args.length == 2) return filterStartingWith(args[1], getCanvasNames());
            }
        }

        return null;
    }

    private List<String> filterStartingWith(String prefix, List<String> options) {
        return options.stream()
                .filter(option -> option.startsWith(prefix))
                .collect(Collectors.toList());
    }

    private List<String> tabCompleteCreateArguments(String[] args, Player player) {
        if (args.length == 2) {
            return filterStartingWith(args[1], getCanvasNames());
        } else if (args.length >= 3 && args.length <= 8) {
            Location targetBlockLocation = player.getTargetBlock(null, 5).getLocation();
            int targetBlockX = targetBlockLocation.getBlockX();
            int targetBlockY = targetBlockLocation.getBlockY();
            int targetBlockZ = targetBlockLocation.getBlockZ();

            switch (args.length) {
                case 3:
                case 6:
                    return Collections.singletonList(Integer.toString(targetBlockX));
                case 4:
                case 7:
                    return Collections.singletonList(Integer.toString(targetBlockY));
                case 5:
                case 8:
                    return Collections.singletonList(Integer.toString(targetBlockZ));
            }
        }

        return null;
    }

    private List<String> getCanvasNames() {
        return Canvas.canvasNames;
    }


}
