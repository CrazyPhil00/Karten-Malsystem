/**
 * TODO
 * - permission check
 *
 */

package me.sieben.malsystem.commands;

import me.sieben.malsystem.MalSystem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;


public class NPCCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(MalSystem.pluginPrefix + "Only Player can perform this command!");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(MalSystem.pluginPrefix + "/help");
            return false;
        }

        switch (args[0]) {
            case ("spawn"):
            {


                Villager Npc = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);

                Npc.setCustomName("Test");
                Npc.setAI(false);


                break;
            }

            case ("remove"):
            {


                break;
            }
        }


        return false;
    }
}
