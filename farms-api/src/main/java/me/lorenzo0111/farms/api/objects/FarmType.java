/*
 * -------------------------------------
 * Copyright Lorenzo0111 2021
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.api.objects;

import org.jetbrains.annotations.Contract;

public enum FarmType {
    BLOCKS(true),
    DROPS(false);

    private final boolean place;

    FarmType(boolean place) {
        this.place = place;
    }

    /**
     * @return true if the base of the farm should be placed
     */
    @Contract(pure = true)
    public boolean place() {
        return place;
    }
}
