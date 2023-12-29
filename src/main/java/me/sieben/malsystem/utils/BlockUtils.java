package me.sieben.malsystem.utils;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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
    BufferedImage image;


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

        return blockUtilsList;
    }

    public static BufferedImage convertToImage(List<BlockUtils> blockUtilsList, int originalSize) {
        int scaledSize = 128;
        int scaleFactor = scaledSize / originalSize;

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

        // Mirror the image horizontally
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);

        // Rotate the image 90 degrees clockwise
        AffineTransform rotateTransform = AffineTransform.getRotateInstance(Math.toRadians(90), scaledSize / 2.0, scaledSize / 2.0);
        AffineTransformOp rotateOp = new AffineTransformOp(rotateTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = rotateOp.filter(image, null);

        return image;
    }

    public static void saveImage(List<BlockUtils> list, int Uid) {

        File configFile = new File("plugins/MalSystem/images/" + Uid +".yml");
        FileConfiguration config;

        if (!configFile.exists()) {

            configFile.getParentFile().mkdirs();
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            config = YamlConfiguration.loadConfiguration(configFile);

            int index = 0;

            for (BlockUtils block : list) {
                config.set(index + ".x", block.getX());
                config.set(index + ".y", block.getY());
                config.set(index + ".color", block.getColor().asRGB());

                index ++;
            }

            config.set("size", index);

            try {
                config.save(configFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static BufferedImage loadImage(short Uid) {

        File configFile = new File("plugins/MalSystem/images/" + Uid +".yml");
        FileConfiguration config;

        if (!configFile.exists()) {
            System.out.println("No image to load!");
            return null;
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        int size = config.getInt("size");

        List<BlockUtils> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            BlockUtils block = new BlockUtils(
                    config.getInt(i + ".x"),
                    config.getInt(i + ".y"),
                    Color.fromRGB(config.getInt(i + ".color"))
            );


            list.add(i, block);
        }

        return BlockUtils.convertToImage(list, (int) Math.sqrt(size));
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

    public void setColor(Color color) {
        this.color = color;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
