/*
 * -------------------------------------
 * Copyright Lorenzo0111 2024
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.api.objects.IFarm;
import me.lorenzo0111.farms.tasks.WorkTask;
import me.lorenzo0111.farms.utils.BlockUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class DataManager {
    private final Farms plugin;
    private final List<IFarm> farms = new ArrayList<>();

    public void init() {
        for (IFarm farm : farms) {
            if (((Farm) farm).getTask() != null)
                continue;

            int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new WorkTask(plugin,farm.getUuid()), 0, plugin.getConfig().getInt("tasks.collect", 10) * 20L);
            ((Farm) farm).setTask(task);
        }
    }

    @SuppressWarnings("unchecked")
    public void reload() {
        if (!plugin.getData().contains("data")) {
            plugin.getData().set("data", new ArrayList<>());
            plugin.reload();
            return;
        }

        final List<Farm> data = (List<Farm>) plugin.getData().getList("data", new ArrayList<>());
        farms.clear();
        farms.addAll(data);
    }

    public void save(boolean disabling) {
        plugin.getData().set("data", farms);
        plugin.reloadData();
        if (!disabling)
            this.init();
    }

    public Farm create(Farm farm) {
        farms.add(farm);
        int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new WorkTask(plugin,farm.getUuid()), 0, plugin.getConfig().getInt("tasks.collect", 10) * 20L);
        farm.setTask(task);
        return farm;
    }

    public boolean contains(Location location) {
        return farms.stream()
                .anyMatch((farm) -> farm.getLocation().equals(location));
    }

    public boolean contains(UUID uuid) {
        return farms.stream()
                .anyMatch((farm) -> farm.getUuid().equals(uuid));
    }

    public @Nullable IFarm get(Location location) {
        return farms.stream()
                .filter((farm) -> farm.getLocation().equals(location))
                .findFirst()
                .orElse(null);
    }

    public @Nullable IFarm get(UUID uuid) {
        return farms.stream()
                .filter((farm) -> farm.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    public @Nullable IFarm find(Block location) {
        return farms.stream()
                .filter((farm) -> {
                    Location posOne = BlockUtils.posOne(farm);
                    Location posTwo = BlockUtils.posTwo(farm);

                    return BlockUtils.inCuboid(location.getLocation(), posOne, posTwo);
                })
                .findFirst()
                .orElse(null);

    }

    public void update(Farm farm) {
        this.getFarms()
                .removeIf((f) -> f.getUuid().equals(farm.getUuid()));
        this.getFarms().add(farm);
    }
}
