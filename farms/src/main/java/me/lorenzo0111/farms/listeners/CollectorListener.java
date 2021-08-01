/*
 * -------------------------------------
 * Copyright Lorenzo0111 2021
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.listeners;

import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.collector.CollectorHandler;
import me.lorenzo0111.farms.hooks.WorldGuardHook;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class CollectorListener implements Listener {
    private final Farms plugin;

    public CollectorListener(Farms plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeave(@NotNull PlayerQuitEvent event) {
        CollectorHandler.removeMember(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onInteract(@NotNull PlayerInteractEvent event) {
        if (!CollectorHandler.canPerform(event.getPlayer())) return;

        Block block = event.getClickedBlock();
        if (block == null || !block.getType().equals(Material.CHEST)) return;

        if (!WorldGuardHook.canBuild(event.getPlayer(), block.getLocation())) return;

        event.setCancelled(true);
        CollectorHandler.complete(event.getPlayer(), block);
    }
}
