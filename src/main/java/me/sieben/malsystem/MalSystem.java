package me.sieben.malsystem;

import me.sieben.malsystem.commands.DesignCommand;
import me.sieben.malsystem.listeners.DesignListener;
import me.sieben.malsystem.utils.BlockUtils;
import me.sieben.malsystem.utils.Canvas;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public final class MalSystem extends JavaPlugin {
    public MalSystem instance;


    public static HashMap<Player, List<Block>> playerBlockList = new HashMap<>();
    public static HashMap<Player, List<BlockUtils>> relativeBlockList = new HashMap<>();
    public static ArrayList<Canvas> canvasList = new ArrayList<>();


    @Override
    public void onEnable() {

        instance = this;
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
        for (String s : getConfig().getStringList("canvas.canvas_names")) {

            System.out.println(s);

            int[] posStart = new int[] {
                    getConfig().getInt("canvas." + s + ".x_pos_1"),
                    getConfig().getInt("canvas." + s + ".y_pos_1"),
                    getConfig().getInt("canvas." + s + ".z_pos_1")};


            int[] posEnd = new int[] {
                    getConfig().getInt("canvas." + s + ".x_pos_2"),
                    getConfig().getInt("canvas." + s + ".y_pos_2"),
                    getConfig().getInt("canvas." + s + ".z_pos_2")};

            Location posTp = new Location(
                    Bukkit.getWorld("world"),
                    getConfig().getInt("canvas." + s + ".x_tp"),
                    getConfig().getInt("canvas." + s + ".y_tp"),
                    getConfig().getInt("canvas." + s + ".z_tp"));

            int width = getConfig().getInt("canvas." + s + ".width");
            int height = getConfig().getInt("canvas." + s + ".height");


            canvasList.add(new Canvas(posStart, posEnd, posTp, width, height));
        }

        return true; // TODO Error handling
    }

    public MalSystem getInstance() {
        return instance;
    }
}
