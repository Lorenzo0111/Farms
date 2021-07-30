/*
 * -------------------------------------
 * Copyright Lorenzo0111 2021
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.api;

import me.lorenzo0111.farms.api.objects.IFarm;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Interface for the FarmsAPI
 */
public interface IFarmsAPI {
    /**
     * @param location Location of the minion
     * @param owner Owner of the farm
     * @return A new farm
     */
    IFarm createFarm(Location location, Player owner);

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
