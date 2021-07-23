package me.lorenzo0111.farms.tasks;

import lombok.RequiredArgsConstructor;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.commands.subcommands.CreateCommand;
import me.lorenzo0111.farms.utils.BlockUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class WorkTask implements Runnable {
    private final Farms plugin;
    private final UUID farm;

    @Override
    public void run() {
        Farm farm = plugin.getDataManager().get(this.farm);
        if (farm == null)
            return;

        int canBreak = farm.getLevel() * 2;
        int broke = 0;

        for (Block block : BlockUtils.near(farm.getLocation().getBlock(), farm.getRadius())) {
            if (broke >= canBreak)
                return;

            if (!block.getType().equals(farm.getBlock()) && !block.getLocation().equals(farm.getLocation())) {
                block.setType(farm.getBlock());
                broke++;
                continue;
            }

            if (block.getType().equals(CreateCommand.getItem().getType()))
                continue;

            if (block.getBlockData() instanceof Ageable) {
                Ageable ageable = (Ageable) block.getBlockData();
                if (ageable.getAge() != ageable.getMaximumAge())
                    continue;

                this.collect(farm,block);
                ageable.setAge(0);
                block.setBlockData(ageable);
            } else {
                this.collect(farm,block);
                block.setType(Material.AIR);
            }

            broke++;
        }
    }

    public void collect(Farm farm, Block block) {
        List<ItemStack> items = farm.getItems();
        Collection<ItemStack> drops = block.getDrops(new ItemStack(Material.DIAMOND_PICKAXE));
        items.addAll(drops);
        plugin.debug("Collecting " + drops + " from " + block.getType() + "...");
        farm.setItems(items);
        plugin.getDataManager().update(farm);
    }
}
