package me.sieben.malsystem.commands;

import me.sieben.malsystem.MalSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MapCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MalSystem.pluginPrefix + "Only Players can perform this Command!");
            return false;
        }

        Player player = (Player) sender;

        if (!(player.hasPermission(MalSystem.getInstance().getConfig().getString("permission.give-map"))))
        {
            player.sendMessage(MalSystem.pluginPrefix + "You don't have the permission to perform that command");
            return false;
        }

        if (args.length == 0) {
            player.sendMessage(getHelp());
            return false;
        }

        switch (args[0])
        {
            case ("give"): {

                if (args.length < 3) {
                    player.sendMessage(getHelp());
                    return false;
                }

                Player target = Bukkit.getPlayer(args[1]);

                int mapId = 0;

                try {
                    mapId = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage(MalSystem.pluginPrefix + args[2] + " is not a number!");
                    return false;
                }

                if (target == null) {
                    player.sendMessage(MalSystem.pluginPrefix + "Player " + args[1] + " isn't Online!");
                    return false;
                }


                ItemStack map = new ItemStack(Material.MAP, 1, (short) mapId);
                MapMeta meta = (MapMeta) map.getItemMeta();
                meta.setDisplayName(ChatColor.DARK_GRAY + "Map of " + ChatColor.BLUE + "" + ChatColor.BOLD + target.getName());
                meta.setLocalizedName(String.valueOf(mapId));
                map.setItemMeta(meta);

                target.getInventory().addItem(map);

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
                MalSystem.pluginPrefix + "§8§l------------§9Help§8§l-----------\n" +
                        MalSystem.pluginPrefix + "/canvas-map give player id:\n" +
                        MalSystem.pluginPrefix + "Gives the specified player the specified Map\n";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        if (args.length == 1) {
            List<String> subcommands = Arrays.asList("give", "help");
            return filterStartingWith(args[0], subcommands);
        }

        return null;
    }

    private List<String> filterStartingWith(String prefix, List<String> options) {
        return options.stream()
                .filter(option -> option.startsWith(prefix))
                .collect(Collectors.toList());
    }


}
