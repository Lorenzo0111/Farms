package me.lorenzo0111.farms.tasks;

import lombok.RequiredArgsConstructor;
import me.lorenzo0111.farms.Farms;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
public class SaveTask implements Runnable {
    private final Farms plugin;

    @Override
    public void run() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getDataManager().save(false);
            plugin.reloadData();
        });
    }
}
