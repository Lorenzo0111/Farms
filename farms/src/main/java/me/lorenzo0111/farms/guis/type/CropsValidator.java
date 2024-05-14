/*
 * -------------------------------------
 * Copyright Lorenzo0111 2024
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.guis.type;

import me.lorenzo0111.farms.Farms;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

public class CropsValidator implements TypeValidator {

    @Override
    public boolean validate(Material material) {
        ConfigurationSection section = Objects.requireNonNull(Farms.getInstance().getConfig().getConfigurationSection("items"));

        return section.contains(material.name());
    }

}
