/*
 * -------------------------------------
 * Copyright Lorenzo0111 2024
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.tasks.worker;

import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.data.DataManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class MinerWorker implements Worker {

    @Override
    public void work(DataManager manager, @NotNull Farm farm) {
        Entity minion = farm.getMinion();
        if(minion == null) return;

        Location location = minion.getLocation();
        location.add(location.getDirection().multiply(2));
        Block block = location.getBlock();

        if (!block.getType().equals(Material.AIR)) {
            this.collect(farm,block);
            block.setType(Material.AIR);
        }
    }

    @Override
    public boolean async() {
        return false;
    }

}
