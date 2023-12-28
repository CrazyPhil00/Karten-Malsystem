package me.sieben.malsystem.utils;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.map.MapPalette;
import org.bukkit.material.Dye;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BlockUtils {

    int x;
    int y;
    Color color;



    public BlockUtils(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }


    public static List<Block> generateBlocks(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);

        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);
        int maxZ = Math.max(z1, z2);

        return IntStream.rangeClosed(minX, maxX)
                .boxed()
                .flatMap(x -> IntStream.rangeClosed(minY, maxY)
                        .boxed()
                        .flatMap(y -> IntStream.rangeClosed(minZ, maxZ)
                                .mapToObj(z -> world.getBlockAt(x, y, z))
                        )
                )
                .collect(Collectors.toList());
    }

    public static List<BlockUtils> convertTo2DList(List<Block> blockList, int size) {
        int minX = blockList.stream().mapToInt(Block::getX).min().orElse(0);
        int minY = blockList.stream().mapToInt(Block::getY).min().orElse(0);
        int maxY = blockList.stream().mapToInt(Block::getY).max().orElse(0);

        List<BlockUtils> blockUtilsList = new ArrayList<>();


        blockList.forEach(block -> {
            int x = block.getX() - minX;
            int y = block.getY() - minY; // Do not adjust maxY here


            if (x >= 0 && x < size && y >= 0 && y < size) {
                blockUtilsList.add(new BlockUtils(x + 1, maxY - y + 1,
                        DyeColor.getByWoolData(block.getData()).getColor())); // Adjust maxY here (MapPalette.matchColor(r, g, b))
            }
        });

        for (BlockUtils blockUtils: blockUtilsList) {
            System.out.println(blockUtils.getX() + ":" + blockUtils.getY() + "/" + blockUtils.getColor());
        }

        return blockUtilsList;
    }

    public static void convertToImage(List<BlockUtils> blockUtilsList, String outputImagePath) {
        int originalSize = 16; // Assuming a 16x16 canvas
        int scaledSize = 128; // Desired 128x128 canvas
        int scaleFactor = scaledSize / originalSize; // Scale factor

        BufferedImage image = new BufferedImage(scaledSize, scaledSize, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < originalSize; i++) {
            for (int j = 0; j < originalSize; j++) {
                int originalX = i;
                int originalY = j;

                for (int k = 0; k < scaleFactor; k++) {
                    for (int l = 0; l < scaleFactor; l++) {
                        int newX = originalX * scaleFactor + k;
                        int newY = originalY * scaleFactor + l;

                        if (newX >= 0 && newX < scaledSize && newY >= 0 && newY < scaledSize) {
                            int index = j * originalSize + i;
                            BlockUtils block = blockUtilsList.get(index);
                            Color color = block.getColor();
                            image.setRGB(newX, newY, color.asRGB());
                        }
                    }
                }
            }
        }

        try {
            File outputFile = new File(outputImagePath);
            ImageIO.write(image, "png", outputFile);
            System.out.println("Image saved to: " + outputImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }












    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
