/*
 * -------------------------------------
 * Copyright Lorenzo0111 2022
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.listeners;

import lombok.RequiredArgsConstructor;
import me.lorenzo0111.farms.Farms;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class JoinListener implements Listener {
    private final Farms plugin;

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("farms.update")) {
            plugin.getUpdater().sendUpdateCheck(event.getPlayer());
        }
    }
}
