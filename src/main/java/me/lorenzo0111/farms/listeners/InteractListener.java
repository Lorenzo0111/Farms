package me.lorenzo0111.farms.listeners;

import dev.triumphteam.gui.guis.Gui;
import lombok.RequiredArgsConstructor;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.guis.FarmGUI;
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

        UUID uuid;
        try {
            uuid = UUID.fromString(event.getRightClicked().getCustomName());
        } catch (IllegalArgumentException ignored) {
            // That entity is not a farms minion
            return;
        }

        if (!plugin.getDataManager().contains(uuid))
            return;

        Farm farm = plugin.getDataManager().get(uuid);
        if (farm == null)
            return;

        if (!farm.getOwner().equals(event.getPlayer().getUniqueId()))
            return;
        event.setCancelled(true);

        Gui gui = new FarmGUI(plugin,event.getPlayer(),farm);
        gui.open(event.getPlayer());
    }
}
