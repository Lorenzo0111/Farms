/*
 * -------------------------------------
 * Copyright Lorenzo0111 2025
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.config.loots;

import com.cryptomorin.xseries.XMaterial;
import lombok.Data;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@Data
public class Loot {
    private final int min;
    private final int max;
    private final XMaterial material;

    public @Nullable ItemStack toItem() {
        int random = (int) (Math.random() * ((max+1) - min)) + min;
        if (random == 0) return null;

        assert material.parseMaterial() != null;
        return new ItemStack(material.parseMaterial(), random);
    }
}
