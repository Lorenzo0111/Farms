/*
 * -------------------------------------
 * Copyright Lorenzo0111 2024
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.api;

import me.lorenzo0111.farms.api.objects.FarmType;
import me.lorenzo0111.farms.api.objects.IFarm;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicesManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Interface for the FarmsAPI
 * You can retrieve it via the {@link ServicesManager#load(Class)} method of {@link Bukkit#getServicesManager()}
 */
public interface IFarmsAPI {
    /**
     * @param location Location of the minion
     * @param owner Owner of the farm
     * @return A new farm
     */
    IFarm createFarm(Location location, Player owner);

    /**
     * @param location Location of the minion
     * @param owner Owner of the farm
     * @param type Farm type
     * @return A new farm
     */
    IFarm createFarm(Location location, Player owner, FarmType type);

    /**
     * @param block A block of the farm
     * @return a farm or null
     */
    @Nullable IFarm getFarm(Block block);

    /**
     * @return A list of loaded farms
     */
    List<IFarm> getFarms();
}
