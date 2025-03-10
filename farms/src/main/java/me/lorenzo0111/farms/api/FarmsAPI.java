/*
 * -------------------------------------
 * Copyright Lorenzo0111 2025
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.api;

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

import java.util.List;
import java.util.UUID;

public class FarmsAPI implements IFarmsAPI {
    private final Farms plugin;

    public FarmsAPI(Farms plugin) {
        this.plugin = plugin;
    }

    @Override
    public IFarm createFarm(Location location, Player owner) {
        return createFarm(location,owner,FarmType.BLOCKS);
    }

    @Override
    public IFarm createFarm(Location location, Player owner, FarmType type) {
        Material before = location.clone().subtract(0,1,0).getBlock().getType();
        Farm farm = plugin.getDataManager().create(new Farm(
                location,
                UUID.randomUUID(),
                owner.getUniqueId(),
                1,
                2,
                type,
                null,
                before));

        StandUtils.miner(location.clone().add(0.5,0,0.5), owner, farm.getUuid().toString());

        location.getBlock().setType(Material.GOLD_BLOCK);

        BlockUtils.full(farm, farm.getLocation());
        return farm;
    }

    @Override
    public IFarm getFarm(Block block) {
        return plugin.getDataManager().find(block);
    }

    @Override
    public List<IFarm> getFarms() {
        return plugin.getDataManager()
                .getFarms();
    }
}
