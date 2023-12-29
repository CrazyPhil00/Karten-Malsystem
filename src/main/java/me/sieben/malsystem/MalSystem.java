package me.sieben.malsystem;

import me.sieben.malsystem.commands.DesignCommand;
import me.sieben.malsystem.listeners.DesignListener;
import me.sieben.malsystem.utils.BlockUtils;
import me.sieben.malsystem.utils.Canvas;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import sun.security.provider.ConfigFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public final class MalSystem extends JavaPlugin {
    public MalSystem() {

    }

    public static HashMap<Player, List<BlockUtils>> relativeBlockList = new HashMap<>();
    public static ArrayList<Canvas> canvasList = new ArrayList<>();

    private File canvasConfigFile;
    private FileConfiguration canvasConfig;

    @Override
    public void onEnable() {

        saveDefaultConfig();


        if (loadCanvas()) System.out.println("Successfully loaded Canvases");
        else System.out.println("Error while loading Canvases");

        getCommand("design").setExecutor(new DesignCommand());

        Bukkit.getPluginManager().registerEvents(new DesignListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean loadCanvas() {
        canvasConfigFile = new File("plugins/MalSystem/canvas.yml");

        if (!canvasConfigFile.exists()) {
            try {
                canvasConfigFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try (FileWriter writer = new FileWriter("plugins/MalSystem/canvas.yml")) {
                // Manually add comments as strings in the YAML file
                writer.write("#\n" +
                        "# Canvas File size (The file size of the images that are being saved):\n" +
                        "# 16 * 16 pixel = 11kb\n" +
                        "# 32 * 32 pixel = 42kb\n" +
                        "# 128 * 128 pixel = 696kb\n" +
                        "#\n");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        canvasConfig = YamlConfiguration.loadConfiguration(canvasConfigFile);

        try {
            canvasConfig.save(canvasConfigFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        if (!(canvasConfig.isSet("canvas.canvas_names"))) return false;

        for (String s : canvasConfig.getStringList("canvas.canvas_names")) {

            int[] posStart = new int[] {
                    canvasConfig.getInt("canvas." + s + ".x_pos_1"),
                    canvasConfig.getInt("canvas." + s + ".y_pos_1"),
                    canvasConfig.getInt("canvas." + s + ".z_pos_1")};


            int[] posEnd = new int[] {
                    canvasConfig.getInt("canvas." + s + ".x_pos_2"),
                    canvasConfig.getInt("canvas." + s + ".y_pos_2"),
                    canvasConfig.getInt("canvas." + s + ".z_pos_2")};

            Location posTp = new Location(
                    Bukkit.getWorld("world"),
                    canvasConfig.getInt("canvas." + s + ".x_tp"),
                    canvasConfig.getInt("canvas." + s + ".y_tp"),
                    canvasConfig.getInt("canvas." + s + ".z_tp"));

            int width = canvasConfig.getInt("canvas." + s + ".width");
            int height = canvasConfig.getInt("canvas." + s + ".height");


            canvasList.add(new Canvas(posStart, posEnd, posTp, width, height));
        }

        return true; // TODO Error handling
    }

}
