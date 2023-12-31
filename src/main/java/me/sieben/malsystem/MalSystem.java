package me.sieben.malsystem;

import me.sieben.malsystem.commands.CreateCanvas;
import me.sieben.malsystem.commands.DesignCommand;
import me.sieben.malsystem.commands.NPCCommand;
import me.sieben.malsystem.listeners.DesignListener;
import me.sieben.malsystem.listeners.NpcListener;
import me.sieben.malsystem.renderer.CanvasRenderer;
import me.sieben.malsystem.utils.BlockUtils;
import me.sieben.malsystem.utils.Canvas;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public final class MalSystem extends JavaPlugin {
    public static MalSystem instance;

    public static HashMap<Player, List<BlockUtils>> relativeBlockList = new HashMap<>();
    public static ArrayList<Canvas> canvasList = new ArrayList<>();
    public static String pluginPrefix;

    public File canvasConfigFile;
    public FileConfiguration canvasConfig;

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        pluginPrefix = getConfig().getString("plugin-prefix").replaceAll("&", "§");


        if (!(getServer().getPluginManager().isPluginEnabled("BankSystem"))) {
            System.out.println(pluginPrefix + "Could not Find Plugin API BankSystem!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (loadCanvases()) System.out.println(pluginPrefix + "Successfully loaded Canvases");
        else System.out.println(pluginPrefix + "Error while loading Canvases");

        getCommand("canvas-npc").setExecutor(new NPCCommand());
        getCommand("canvas-npc").setTabCompleter(new NPCCommand());

        getCommand("design").setExecutor(new DesignCommand());

        getCommand("canvas").setExecutor(new CreateCanvas());
        getCommand("canvas").setTabCompleter(new CreateCanvas());


        Bukkit.getPluginManager().registerEvents(new DesignListener(), this);
        Bukkit.getPluginManager().registerEvents(new NpcListener(), this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public boolean loadCanvases() {

        canvasConfigFile = new File("plugins/CanvasMap/canvas.yml");

        if (!canvasConfigFile.exists()) {
            try {
                canvasConfigFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            try (FileWriter writer = new FileWriter("plugins/CanvasMap/canvas.yml")) {
                // Manually add comments as strings in the YAML file
                writer.write("#\n" +
                        "#Supported Canvas sizes: 2, 4, 8, 16, 32, 64, 128\n" +
                        "# Canvas File size (The file size of the images that are being saved):\n" +
                        "# 16 * 16 pixel = 11kb\n" +
                        "# 32 * 32 pixel = 42kb\n" +
                        "# 128 * 128 pixel = 696kb\n" +
                        "#\n");

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        canvasConfig = YamlConfiguration.loadConfiguration(canvasConfigFile);

        try {
            canvasConfig.save(canvasConfigFile);
        } catch (IOException e) {
            return false;
        }


        if (!(canvasConfig.isSet("canvas.canvas_names"))) {
            System.out.println(pluginPrefix + "No Canvases to load!");
            return true;
        }

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

            Canvas.canvasNames.add(s);
            canvasList.add(new Canvas(posStart, posEnd, posTp, width, height));
        }




        File dir = new File("plugins/CanvasMap/images");

        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".yml")) {

                short uID = (short) Integer.parseInt(file.getName().replaceAll(".yml", ""));

                MapView view = Bukkit.getMap(uID);

                for (MapRenderer renderer : view.getRenderers()) {
                    view.removeRenderer(renderer);
                }

                CanvasRenderer mapRenderer = new CanvasRenderer();
                BufferedImage image = BlockUtils.loadImage(uID);
                mapRenderer.loadImage(image);
                view.addRenderer(mapRenderer);

            }
        }

        return true;

    }


    public static MalSystem getInstance() {
        return instance;
    }
}
