package me.sieben.malsystem.utils;

import org.bukkit.Location;

import java.util.ArrayList;

public class Canvas {

    int[] canvasPosStart;
    int[] canvasPosEnd;
    Location tpPos;
    boolean inUse;

    public Canvas(int [] canvasPosStart, int[] canvasPosEnd, Location tpPos) {
        this.canvasPosStart = canvasPosStart;
        this.canvasPosEnd = canvasPosEnd;
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

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public static Canvas getEmpty(ArrayList<Canvas> canvas) {
        for (Canvas c : canvas) {
            if (!(c.inUse)) {
                System.out.println(c.getCanvasPosEnd());
                c.setInUse(true);
                return c;
            }
        }
        return null; //TODO No empty Canvas Error
    }
}
