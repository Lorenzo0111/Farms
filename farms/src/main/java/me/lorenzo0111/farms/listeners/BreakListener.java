/*
 * -------------------------------------
 * Copyright Lorenzo0111 2021
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.listeners;

import lombok.RequiredArgsConstructor;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;

@RequiredArgsConstructor
public class BreakListener implements Listener {
    private final Farms plugin;

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        if (!plugin.getDataManager().contains(event.getBlock().getLocation())) {
            Farm farm = plugin.getDataManager().find(event.getBlock());
            if (farm != null)
                event.setCancelled(true);
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onMove(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            if (plugin.getDataManager().contains(block.getLocation())) {
                event.setCancelled(true);
            }
        }
    }
}
