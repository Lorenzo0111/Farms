/*
 * -------------------------------------
 * Copyright Lorenzo0111 2021
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.api;

import com.cryptomorin.xseries.XMaterial;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.api.objects.FarmType;
import me.lorenzo0111.farms.api.objects.IFarm;
import me.lorenzo0111.farms.utils.BlockUtils;
import me.lorenzo0111.farms.utils.StandUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FarmsAPI implements IFarmsAPI {
    private final Farms plugin;

    public FarmsAPI(Farms plugin) {
        this.plugin = plugin;
    }

    @Override
    public IFarm createFarm(Location location, Player player) {
        Material before = location.clone().subtract(0,1,0).getBlock().getType();
        Farm farm = plugin.getDataManager().create(new Farm(
                location,
                UUID.randomUUID(),
                player.getUniqueId(),
                1,
                2,
                FarmType.BLOCKS,
                Material.WHEAT,
                before));

        StandUtils.miner(location.clone().add(0.5,0,0.5), player, farm.getUuid().toString());

        location.getBlock().setType(Material.GOLD_BLOCK);
        BlockUtils.near(location.getBlock(), 2).forEach(block -> {
            if (block.getType().equals(Material.DIRT) || block.getType().equals(XMaterial.GRASS_BLOCK.parseMaterial()) || block.getType().equals(Material.AIR)) {
                block.setType(XMaterial.FARMLAND.parseMaterial());
            }
        });

        BlockUtils.full(farm, farm.getLocation());
        return farm;
    }

    @Override
    public IFarm getFarm(Block block) {
        return plugin.getDataManager().find(block);
    }

    @Override
    public List<IFarm> getFarms() {
        return new ArrayList<>(plugin.getDataManager()
                .getFarms());
    }
}
