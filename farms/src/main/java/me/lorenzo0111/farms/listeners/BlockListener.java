package me.lorenzo0111.farms.listeners;

import lombok.RequiredArgsConstructor;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

@RequiredArgsConstructor
public class BlockListener implements Listener {
    private final Farms plugin;

    @EventHandler(ignoreCancelled = true)
    public void onBlockEdit(BlockBreakEvent event) {
        Farm farm = plugin.getDataManager().find(event.getBlock());
        if (farm == null)
            return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPhysics(BlockPhysicsEvent event) {
        Farm farm = plugin.getDataManager().find(event.getBlock());
        if (farm == null)
            return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onChange(EntityChangeBlockEvent event) {
        Farm farm = plugin.getDataManager().find(event.getBlock());
        if (farm == null)
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onFade(BlockFadeEvent event) {
        Farm farm = plugin.getDataManager().find(event.getBlock());
        if (farm == null)
            return;

        event.setCancelled(true);
    }

}