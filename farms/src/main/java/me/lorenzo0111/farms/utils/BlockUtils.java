package me.lorenzo0111.farms.utils;

import me.lorenzo0111.farms.api.objects.Farm;
import org.apache.commons.lang.math.IntRange;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BlockUtils {

    public static ArrayList<Block> near(Block center, int radius) {
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
        return new IntRange(pos1.getX(), pos2.getX()).containsDouble(block.getX())
                && new IntRange(pos1.getY(), pos2.getY()).containsDouble(block.getY())
                &&  new IntRange(pos1.getZ(), pos2.getZ()).containsDouble(block.getZ());
    }

    public static Location posOne(Farm farm) {
        return farm.getLocation().clone().add(farm.getRadius(),0,farm.getRadius());
    }

    public static Location posTwo(Farm farm) {
        return farm.getLocation().clone().subtract(farm.getRadius(),1,farm.getRadius());
    }

    public static void full(Farm farm) {
        Block item = farm.getLocation().getBlock();
        BlockUtils.near(item, 2).forEach(block -> {
            if (block.getType().equals(Material.AIR)) {
                block.setType(farm.getBlock());
            }
        });
    }

    public static void full(Farm farm, Material old) {
        Block item = farm.getLocation().getBlock();
        BlockUtils.near(item, 2).forEach(block -> {
            if (block.getType().equals(Material.AIR) || block.getType().equals(old)) {
                block.setType(farm.getBlock());
            }
        });
    }

    public static void full(Farm farm, Location... ignore) {
        Block item = farm.getLocation().getBlock();
        List<Location> ignoreList = Arrays.asList(ignore);
        BlockUtils.near(item, 2).forEach(block -> {
            if (block.getType().equals(Material.AIR) && !ignoreList.contains(block.getLocation())) {
                block.setType(farm.getBlock());
            }
        });
    }

}
