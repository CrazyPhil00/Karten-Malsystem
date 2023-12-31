package me.sieben.malsystem.gui;

import com.sun.org.apache.xpath.internal.axes.FilterExprWalker;
import me.sieben.malsystem.MalSystem;
import me.sieben.malsystem.commands.NPCCommand;
import me.sieben.malsystem.utils.Canvas;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NPCGui {

    private FileConfiguration config = MalSystem.getInstance().getConfig();
    private static ArrayList<Villager> NPCList = new ArrayList<>();

    public void openGuiCreateMap(Player player) {

        Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST,
                "      §8§kk§r §2§lSelect Map Size §8§kk");

        String config_path = "npc.select-canvas.item-slot.";

        for (int i = 0; i < 26; i++) {
            if (config.isSet(config_path + i)) {

                int canvas_size = config.getInt(config_path + i + ".canvas-size");

                ItemStack stack;
                ItemMeta meta;

                if (Canvas.getFreeCanvas(canvas_size) <= 0) {
                    stack = new ItemStack(Material.BARRIER);
                }else stack = new ItemStack(Material.MAP);

                meta = stack.getItemMeta();

                meta.setDisplayName(config.getString(config_path + i + ".name").replaceAll("&", "§"));
                meta.setLocalizedName(String.valueOf(canvas_size));

                List<String> lore = new ArrayList<>();

                for (int j = 1; j < 4; j++) {
                    if (config.isSet(config_path + i + ".lore-" + j)) {
                        if (!(config.getString(config_path + i + ".lore-" + j).equalsIgnoreCase(""))) {
                            lore.add(config.getString(config_path + i + ".lore-" + j).
                                    replaceAll("&", "§").
                                    replaceAll("%empty-canvas-amount%",
                                            String.valueOf(Canvas.getFreeCanvas(canvas_size))));
                        }
                    }
                }

                meta.setLore(lore);
                stack.setItemMeta(meta);

                inventory.setItem(i, stack);

            }
        }

        player.openInventory(inventory);

    }

    public void openGuiSaveMap(Player player) {

        Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST,
                "      §8§kk§r §2§lBuy §7or §c§lExit §8§kk");

        String config_path = "npc.save-canvas.item-slot.";

        for (int i = 0; i < 26; i++) {
            if (config.isSet(config_path + i)) {

                if (Material.getMaterial(config.getString(config_path + i + ".material")) == null) {
                    System.out.println(MalSystem.pluginPrefix + "Could not find Material: " + config.getString(config_path + i + ".material"));
                    return;
                }

                ItemStack stack = new ItemStack(Material.getMaterial(config.getString(config_path + i + ".material")));
                ItemMeta meta = stack.getItemMeta();

                meta.setDisplayName(config.getString(config_path + i + ".name").replaceAll("&", "§"));

                List<String> lore = new ArrayList<>();

                for (int j = 1; j < 4; j++) {
                    if (config.isSet(config_path + i + ".lore-" + j)) {
                        if (!(config.getString(config_path + i + ".lore-" + j).equalsIgnoreCase(""))) {
                            lore.add(config.getString(config_path + i + ".lore-" + j).
                                    replaceAll("&", "§"));
                        }
                    }
                }

                meta.setLore(lore);
                stack.setItemMeta(meta);

                inventory.setItem(i, stack);

            }
        }


        player.openInventory(inventory);

    }

    public void confirmExit(Player player) {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST,
                "      §8§kk§r §c§lConfirm Exit §8§kk");

        ItemStack stack = new ItemStack(Material.BARRIER);
        ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName("§c§lExit");

        List<String> exitLore = new ArrayList<>();
        exitLore.add("§4Your painting will not be saved!");

        meta.setLore(exitLore);

        stack.setItemMeta(meta);

        inventory.setItem(13, stack);
        player.openInventory(inventory);
    }

    public static File npcFile = new File("plugins/CanvasMap/npc.yml");
    public static FileConfiguration npcConfig = YamlConfiguration.loadConfiguration(npcFile);

    public static void saveNPC(Entity entity, String npcType) {

        if (!(npcFile.exists())) {
            try {
                npcFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (entity == null) return;

        npcConfig.set(entity.getUniqueId().toString(), npcType);

        try {
            npcConfig.save(npcFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
