/*
 * -------------------------------------
 * Copyright Lorenzo0111 2021
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.tasks;

import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QueueTask implements Runnable {
    private final Farms plugin;

    public QueueTask(Farms plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            List<Farm> farms = plugin.getDataManager()
                    .getFarms()
                    .stream()
                    .filter(farm -> farm.getCollector() != null)
                    .collect(Collectors.toList());

            for (Farm farm : farms) {
                Location collector = farm.getCollector();
                if (collector == null || !collector.isWorldLoaded()) continue;
                if (!collector.getBlock().getType().equals(Material.CHEST)) continue;

                Chest chest = (Chest) collector.getBlock().getState();
                Inventory inventory = chest.getInventory();
                List<ItemStack> items = new ArrayList<>(farm.getItems());
                farm.getItems().clear();
                for (ItemStack item : items) {
                    inventory.addItem(item);
                }
            }
        });
    }
}
