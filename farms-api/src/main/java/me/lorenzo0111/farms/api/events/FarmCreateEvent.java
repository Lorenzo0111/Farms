/*
 * -------------------------------------
 * Copyright Lorenzo0111 2022
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.api.events;

import me.lorenzo0111.farms.api.objects.IFarm;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class FarmCreateEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private final IFarm farm;

    public FarmCreateEvent(@NotNull Player who, IFarm farm) {
        super(who);
        this.farm = farm;
    }

    public IFarm getFarm() {
        return farm;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
