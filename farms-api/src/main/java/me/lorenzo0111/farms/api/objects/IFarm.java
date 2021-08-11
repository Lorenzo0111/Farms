/*
 * -------------------------------------
 * Copyright Lorenzo0111 2021
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.api.objects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * A farm object
 */
public interface IFarm {
    /**
     * @return Location of the minion
     */
    Location getLocation();


    /**
     * Set farm location
     * @param location New location
     */
    void setLocation(Location location);

    /**
     * @return Location of the collector
     */
    Location getCollector();

    /**
     * Set collector location
     * @param location New location
     */
    void setCollector(Location location);

    /**
     * @return Farm UUID
     */
    UUID getUuid();

    /**
     * @return UUID of the owner of the farm
     */
    UUID getOwner();

    /**
     * @return Level of the farm
     */
    int getLevel();

    /**
     * @return Radius of the farm. Default: 2
     */
    int getRadius();

    /**
     * @return Type of the farm. What is the minion farming?
     */
    FarmType getType();

    /**
     * @return Block to farm
     */
    Material getBlock();

    /**
     * Destroy deleting it from the database
     */
    void destroy();
    /**
     * Remove the farm without deleting it from the database.
     */
    void safeDestroy();

    /**
     * Pick the farm item to an entity
     * @param entity Entity to give the item
     */
    void pickup(@NotNull HumanEntity entity);
}
