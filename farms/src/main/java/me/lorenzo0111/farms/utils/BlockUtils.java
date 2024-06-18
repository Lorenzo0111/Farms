/*
 * -------------------------------------
 * Copyright Lorenzo0111 2024
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.utils;

import me.lorenzo0111.farms.api.objects.IFarm;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BlockUtils {

    public static List<Block> near(Block center, int radius) {
        ArrayList<Block> blocks = new ArrayList<>();
        for(double x = center.getLocation().getX() - radius; x <= center.getLocation().getX() + radius; x++){
            for(double z = center.getLocation().getZ() - radius; z <= center.getLocation().getZ() + radius; z++){
                Location loc = new Location(center.getWorld(), x, center.getY(), z);
                blocks.add(loc.getBlock());
            }
        }
        return blocks;
    }

    public static boolean inCuboid(Location block, Location pos1, Location pos2){
        double minX = Math.min(pos1.getX(), pos2.getX());
        double maxX = Math.max(pos1.getX(), pos2.getX());
        double minY = Math.min(pos1.getY(), pos2.getY());
        double maxY = Math.max(pos1.getY(), pos2.getY());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());

        boolean containsX = block.getX() >= minX && block.getX() <= maxX;
        boolean containsY = block.getY() >= minY && block.getY() <= maxY;
        boolean containsZ = block.getZ() >= minZ && block.getZ() <= maxZ;

        return containsX && containsY && containsZ;
    }

    public static Location posOne(IFarm farm) {
        return farm.getLocation().clone().add(farm.getRadius(),0,farm.getRadius());
    }

    public static Location posTwo(IFarm farm) {
        return farm.getLocation().clone().subtract(farm.getRadius(),1,farm.getRadius());
    }

    public static void full(IFarm farm) {
        Block item = farm.getLocation().getBlock();
        BlockUtils.near(item, farm.getRadius()).forEach(block -> {
            if (block.getType().equals(Material.AIR)) {
                block.setType(farm.getBlock());
            }
        });
    }

    public static void full(IFarm farm, Material old) {
        Block item = farm.getLocation().getBlock();
        BlockUtils.near(item, farm.getRadius()).forEach(block -> {
            if (block.equals(item)) return;

            if (block.getType().equals(Material.AIR) || block.getType().equals(old)) {
                block.setType(farm.getBlock());
            }
        });
    }

    public static void full(IFarm farm, Location... ignore) {
        Block item = farm.getLocation().getBlock();
        List<Location> ignoreList = Arrays.asList(ignore);
        BlockUtils.near(item, farm.getRadius()).forEach(block -> {
            if (block.getType().equals(Material.AIR) && !ignoreList.contains(block.getLocation())) {
                block.setType(farm.getBlock());
            }
        });
    }

}
