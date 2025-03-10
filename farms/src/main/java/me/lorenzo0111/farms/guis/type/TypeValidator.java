/*
 * -------------------------------------
 * Copyright Lorenzo0111 2025
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.guis.type;

import org.bukkit.Material;

public interface TypeValidator {

    /**
     * @param material The material to check
     * @return true if the farm can use that material
     */
    boolean validate(Material material);

}
