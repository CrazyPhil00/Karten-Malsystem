package me.sieben.malsystem.renderer;

import me.sieben.malsystem.MalSystem;
import me.sieben.malsystem.utils.BlockUtils;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.util.List;


public class CanvasRenderer extends MapRenderer {


    private List<BlockUtils> blockList;
    private boolean done;

    public CanvasRenderer() {

    }

    public CanvasRenderer(List<BlockUtils> blockList) {
        this.blockList = blockList;
        done = false;
    }

    public boolean loadBlockList(List<BlockUtils> blockList) {
        this.blockList = blockList;

        return true;
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        if (done) return;


        for (BlockUtils block : blockList) {

            int r = block.getColor().getRed();
            int g = block.getColor().getGreen();
            int b = block.getColor().getBlue();




        }

        done = true;
    }

}
