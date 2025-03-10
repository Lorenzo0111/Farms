/*
 * -------------------------------------
 * Copyright Lorenzo0111 2025
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.api.objects;

import org.jetbrains.annotations.Contract;

/**
 * Type of the farm
 */
public enum FarmType {
    BLOCKS(true, true),
    DROPS(false, false),
    MOB(false, false),
    MINER(false, false),
    SPAWNER(false, true);

    private final boolean place;
    private final boolean select;

    /**
     * @param place Should the farm place and protect blocks?
     * @param select Can the user select a material for the farm?
     */
    FarmType(boolean place, boolean select) {
        this.place = place;
        this.select = select;
    }

    /**
     * @return true if the farm should place and protect blocks?
     */
    @Contract(pure = true)
    public boolean place() {
        return place;
    }

    /**
     * @return true if the user can select a material for the farm
     */
    public boolean canSelect() {
        return select;
    }
}
