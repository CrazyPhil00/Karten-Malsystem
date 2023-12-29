package me.sieben.malsystem.utils;

import org.bukkit.Location;

import java.util.ArrayList;

public class Canvas {

    public static ArrayList<String> canvasNames = new ArrayList<>();

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
        return null; //TODO No empty Canvas Error
    }
}
