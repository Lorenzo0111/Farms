package me.lorenzo0111.farms.tasks;

import com.cryptomorin.xseries.XBlock;
import lombok.RequiredArgsConstructor;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.commands.subcommands.CreateCommand;
import me.lorenzo0111.farms.utils.BlockUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class WorkTask implements Runnable {
    private final Farms plugin;
    private final UUID farm;

    @Override
    @SuppressWarnings("deprecation")
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

            if (block.getType().equals(CreateCommand.getItem().getItem().getType()))
                continue;

            if (block.getState().getData() instanceof Crops) {
                if (XBlock.getAge(block) != 7)
                    continue;

                this.collect(farm,block);
                XBlock.setAge(block,0);
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
