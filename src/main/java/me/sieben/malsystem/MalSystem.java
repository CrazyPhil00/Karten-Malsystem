package me.sieben.malsystem;

import me.sieben.malsystem.commands.DesignCommand;
import me.sieben.malsystem.listeners.DesignListener;
import me.sieben.malsystem.utils.Canvas;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class MalSystem extends JavaPlugin {
    public MalSystem instance;


    public static ArrayList<Canvas> canvasList = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;
        saveDefaultConfig();


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

            canvasList.add(new Canvas(posStart, posEnd, posTp));
        }



        getCommand("design").setExecutor(new DesignCommand());

        Bukkit.getPluginManager().registerEvents(new DesignListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public MalSystem getInstance() {
        return instance;
    }
}
