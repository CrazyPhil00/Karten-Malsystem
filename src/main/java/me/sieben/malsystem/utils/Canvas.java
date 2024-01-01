package me.sieben.malsystem.utils;

import me.sieben.malsystem.MalSystem;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Canvas {

    public static ArrayList<String> canvasNames = new ArrayList<>();
    public static HashMap<Player, Canvas> assignedPlayers = new HashMap<>();
    public static HashMap<Player, Location> oldPlayerPos = new HashMap<>();

    int[] canvasPosStart;
    int[] canvasPosEnd;
    int width;
    int height;
    Location tpPos;
    boolean inUse;


    public Canvas(int [] canvasPosStart, int[] canvasPosEnd, Location tpPos, int width, int height ) {
        this.canvasPosStart = canvasPosStart;
        this.canvasPosEnd = canvasPosEnd;
        this.width = width;
        this.height = height;
        this.tpPos = tpPos;
        this.inUse = false;
    }

    public int[] getCanvasPosEnd() {
        return canvasPosEnd;
    }

    public int[] getCanvasPosStart() {
        return canvasPosStart;
    }

    public Location getTpPos() {
        return tpPos;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public static Canvas getEmpty(ArrayList<Canvas> canvas, int width, int height) {
        for (Canvas c : canvas) {
            if (!(c.isInUse())) {
                if (c.getWidth() == width && c.getHeight() == height) {
                    c.setInUse(true);
                    return c;
                }
            }
        }
        return null; // TODO No empty Canvas Error
    }

    public static int getFreeCanvas(int size) {
        int i = 0;
        for (Canvas canvas : MalSystem.canvasList) {
            if ((!canvas.isInUse()) && canvas.getWidth() == size) {
                i ++;
            }
        }
        return i;
    }

    public static boolean createCanvas(Player player, int width, int height) {
        if (assignedPlayers.containsKey(player)) {
            player.sendMessage(MalSystem.pluginPrefix + "You are already using a canvas.");
            return false;
        }
        Canvas canvas = Canvas.getEmpty(MalSystem.canvasList, width, height);

        if (canvas == null) {
            player.sendMessage(MalSystem.pluginPrefix + "Error while Creating Canvas.");
            return false;
        }

        assignedPlayers.put(player, canvas);

        oldPlayerPos.put(player, player.getLocation());
        player.teleport(canvas.getTpPos());

        CanvasUtils.saveInv(player);
        CanvasUtils.giveColors(player);

        return true;
    }

    public static Canvas getAssignedPlayer(Player player) {
        return assignedPlayers.get(player);
    }

    public static boolean containsAssignedPlayer(Player player) {
        return assignedPlayers.containsKey(player);
    }

    public static void removeAssignedPlayer(Player player) {
        assignedPlayers.get(player).setInUse(false);
        assignedPlayers.remove(player);
    }
}
