/*
 * -------------------------------------
 * Copyright Lorenzo0111 2022
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.tasks.worker;

import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.data.DataManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

public interface Worker {
    void work(DataManager manager, Farm farm);
    boolean async();

    default void collect(Farm farm, Block block) {
        Collection<ItemStack> drops = block.getDrops(new ItemStack(Material.DIAMOND_PICKAXE));
        this.collect(farm, drops);
    }

    default void collect(Farm farm, Collection<ItemStack> drops) {
        Farms.getInstance().debug("Collecting " + drops.size() + " drops..");
        List<ItemStack> items = farm.getItems();
        items.addAll(drops);
        farm.setItems(items);
        Farms.getInstance().getDataManager().update(farm);
    }
}
