package me.sieben.malsystem.renderer;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;


public class CanvasRenderer extends MapRenderer {


    private boolean done;
    private BufferedImage image;

    public CanvasRenderer() {
        done = false;
    }

    public void loadImage(BufferedImage image) {
        this.image = image;
        done = false;
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        if (done) return;

        mapCanvas.drawImage(0, 0, image);
        done = true;
    }

}
