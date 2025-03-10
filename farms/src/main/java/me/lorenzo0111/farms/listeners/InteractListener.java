/*
 * -------------------------------------
 * Copyright Lorenzo0111 2025
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.listeners;

import dev.triumphteam.gui.guis.Gui;
import lombok.RequiredArgsConstructor;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.api.objects.IFarm;
import me.lorenzo0111.farms.guis.FarmGUI;
import me.lorenzo0111.farms.utils.StandUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.UUID;

@RequiredArgsConstructor
public class InteractListener implements Listener {
    private final Farms plugin;

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof ArmorStand))
            return;

        if (event.getRightClicked().getCustomName() == null)
            return;

        UUID uuid = StandUtils.farm(event.getRightClicked());
        if (uuid == null) {
            return;
        }

        if (!plugin.getDataManager().contains(uuid))
            return;

        IFarm farm = plugin.getDataManager().get(uuid);
        if (farm == null)
            return;
        event.setCancelled(true);

        if (!farm.getOwner().equals(event.getPlayer().getUniqueId()))
            return;

        Gui gui = new FarmGUI(plugin,event.getPlayer(),(Farm) farm);
        gui.open(event.getPlayer());
    }
}
