/*
 * -------------------------------------
 * Copyright Lorenzo0111 2022
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.guis.type;

import org.bukkit.Material;

public class SpawnerValidator implements TypeValidator {

    @Override
    public boolean validate(Material material) {
        return material.name().contains("_SPAWN_EGG");
    }

}
