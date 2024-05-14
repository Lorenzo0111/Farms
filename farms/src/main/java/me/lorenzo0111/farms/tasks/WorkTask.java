/*
 * -------------------------------------
 * Copyright Lorenzo0111 2024
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.tasks;

import lombok.RequiredArgsConstructor;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.api.objects.FarmType;
import me.lorenzo0111.farms.api.objects.IFarm;
import me.lorenzo0111.farms.tasks.worker.*;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class WorkTask implements Runnable {
    private final Farms plugin;
    private final UUID farm;
    private static final Map<FarmType, Worker> WORKERS = new HashMap<>();

    static {
        WORKERS.put(FarmType.BLOCKS, new BlocksWorker());
        WORKERS.put(FarmType.DROPS, new DropsWorker());
        WORKERS.put(FarmType.MOB, new MobWorker());
        WORKERS.put(FarmType.MINER, new MinerWorker());
        WORKERS.put(FarmType.SPAWNER, new SpawnerWorker());
    }

    @Override
    public void run() {
        IFarm farm = plugin.getDataManager().get(this.farm);

        if (farm == null) return;

        if (!WORKERS.containsKey(farm.getType())) {
            plugin.getLogger().warning("Unable to find a free worker for " + farm.getType());
            return;
        }

        Worker worker = WORKERS.get(farm.getType());
        if (worker.async()) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> worker.work(plugin.getDataManager(),(Farm)farm));
            return;
        }

        worker.work(plugin.getDataManager(),(Farm)farm);
        plugin.debug("Worked on farm &e" + farm.getUuid());
    }
}
