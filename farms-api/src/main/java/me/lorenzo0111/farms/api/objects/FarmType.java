/*
 * -------------------------------------
 * Copyright Lorenzo0111 2021
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.api.objects;

import org.jetbrains.annotations.Contract;

/**
 * Type of the farm
 */
public enum FarmType {
    BLOCKS(true),
    DROPS(false),
    MOB(false),
    MINER(false);

    private final boolean place;

    /**
     * @param place Should the farm place and protect blocks?
     */
    FarmType(boolean place) {
        this.place = place;
    }

    /**
     * @return true if the farm should place and protect blocks?
     */
    @Contract(pure = true)
    public boolean place() {
        return place;
    }
}
