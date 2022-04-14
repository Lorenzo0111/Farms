/*
 * -------------------------------------
 * Copyright Lorenzo0111 2022
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.tasks.worker;

import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.data.DataManager;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

public class SpawnerWorker implements Worker {

    @Override
    public void work(DataManager manager, Farm farm) {
        Material type = farm.getBlock();
        String entityName = type.name().replace("_SPAWN_EGG", "");

        try {
            EntityType entityType = EntityType.valueOf(entityName);
            World world = farm.getLocation().getWorld();

            if (world == null) {
                return;
            }

            world.spawnEntity(farm.getLocation(), entityType);
        } catch (IllegalArgumentException ignored) {
            Farms.getInstance().debug("Invalid entity type: " + entityName);
        }
    }

    @Override
    public boolean async() {
        return false;
    }

}
