/**
 * TODO
 * - permission check
 *
 */

package me.sieben.malsystem.commands;

import me.sieben.malsystem.MalSystem;
import me.sieben.malsystem.gui.NPCGui;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class NPCCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(MalSystem.pluginPrefix + "Only Player can perform this command!");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(getHelp());
            return false;
        }

        switch (args[0]) {
            case ("spawn"):
            {

                if (args.length < 3) {
                    player.sendMessage(getHelp());
                    return false;
                }

                Villager Npc = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);

                Npc.setCustomName(args[1].replaceAll("&", "§"));

                Npc.setAI(false);
                Npc.setSilent(true);
                Npc.setInvulnerable(true);

                NPCGui.saveNPC(Npc, args[2]);

                break;
            }

            case ("remove"):
            {
                Collection<Entity> entityList = player.getWorld().getNearbyEntities(
                        player.getLocation(),
                        4, 4, 4);

                for (Entity entity: entityList) {
                    if (entity.getType() == EntityType.VILLAGER) {
                        if (NPCGui.npcConfig.isSet(entity.getUniqueId().toString())) {
                            entity.remove();
                            player.sendMessage(MalSystem.pluginPrefix + "Removed Entity " + entity.getCustomName());
                        }
                    }
                }

                break;
            }

            case ("help"): {
                player.sendMessage(getHelp());
            }
        }


        return false;
    }


    private String getHelp() {
        return
                MalSystem.pluginPrefix + "§8§l------§9Help§8§l------\n" +
                MalSystem.pluginPrefix + "/canvas-npc §aspawn §d§oname §b§onpc-type §f:\n" +
                MalSystem.pluginPrefix + "Spawns a NPC at the executing Players position\n" +
                MalSystem.pluginPrefix + "- npc-type: §aCREATE-CANVAS§7/§6SAVE-CANVAS\n" +
                MalSystem.pluginPrefix + "/canvas-npc §cremove §f: \n\n" +
                MalSystem.pluginPrefix + "Removes all Canvas NPC's in a 4 block radius\n";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (!(sender instanceof Player)) {
            return completions;
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            // First argument completion
            completions.add("spawn");
            completions.add("remove");
            completions.add("status");
        } else if (args.length == 3 && args[0].equalsIgnoreCase("spawn")) {
            // Second argument completion for the 'spawn' command
            // You can add more options based on your requirements
            completions.add("SAVE-CANVAS");
            completions.add("CREATE-CANVAS");
        }

        // Add more completions for additional arguments as needed

        return completions;
    }
}
