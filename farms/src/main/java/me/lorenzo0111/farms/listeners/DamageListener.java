/*
 * -------------------------------------
 * Copyright Lorenzo0111 2024
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.listeners;

import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.utils.StandUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DamageListener implements Listener {
    private final Farms plugin;

    public DamageListener(Farms plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(@NotNull EntityDamageEvent event) {
        UUID farm = StandUtils.farm(event.getEntity());
        if (farm != null && plugin.getDataManager().contains(farm)) event.setCancelled(true);
    }
}
