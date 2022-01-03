/*
 * -------------------------------------
 * Copyright Lorenzo0111 2022
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.tasks.worker;

import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.config.loots.Loot;
import me.lorenzo0111.farms.data.DataManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MobWorker implements Worker {

    @Override
    public void work(DataManager manager, @NotNull Farm farm) {
        Entity minion = farm.getMinion();
        if (minion == null) return;

        List<Entity> nearbyEntities = minion.getNearbyEntities(farm.getRadius(), farm.getRadius(), farm.getRadius());
        for (Entity entity : nearbyEntities) {
            if (!(entity instanceof LivingEntity)) continue;
            if (entity.getType().equals(EntityType.HORSE) || entity.getType().equals(minion.getType()) || entity.getType().equals(EntityType.PLAYER)) continue;

            List<ItemStack> items = new ArrayList<>();
            List<Loot> loots = manager.getPlugin().getLoots().getLoots(entity.getType());
            for (Loot loot : loots) {
                ItemStack item = loot.toItem();
                if (item == null) continue;
                items.add(item);
            }

            collect(farm,items);
            entity.remove();
        }
    }

    @Override
    public boolean async() {
        return false;
    }
}
