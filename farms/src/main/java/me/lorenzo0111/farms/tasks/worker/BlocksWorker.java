/*
 * -------------------------------------
 * Copyright Lorenzo0111 2021
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.tasks.worker;

import com.cryptomorin.xseries.XBlock;
import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.commands.subcommands.CreateCommand;
import me.lorenzo0111.farms.data.DataManager;
import me.lorenzo0111.farms.utils.BlockUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.Crops;

import java.util.ArrayList;
import java.util.List;

public class BlocksWorker implements Worker {

    @SuppressWarnings("deprecation")
    @Override
    public void work(DataManager manager, Farm farm) {
        if (farm == null)
            return;

        List<Block> willBreak = new ArrayList<>();
        List<Block> willChange = new ArrayList<>();

        for (final Block block : BlockUtils.near(farm.getLocation().getBlock(), farm.getRadius())) {
            if (!block.getType().equals(farm.getBlock()) && !block.getLocation().equals(farm.getLocation())) {
                willChange.add(block);
                continue;
            }

            if (block.getType().equals(CreateCommand.getItem().getItem().getType()))
                continue;

            if (block.getState().getData() instanceof Crops && XBlock.getAge(block) != 7) {
                continue;
            }

            willBreak.add(block);
        }

        final int size = willBreak.size() + willChange.size();
        final double canDo = Math.floor(manager.getPlugin().getConfig().getDouble("levels." + farm.getLevel()) / 100 * size);
        int done = 0;

        for (final Block block : willBreak) {
            if (canDo <= done) break;

            this.collect(farm,block);
            block.setType(Material.AIR);
            done++;
        }

        for (final Block block : willChange) {
            if (canDo <= done) break;

            block.setType(farm.getBlock());
            done++;
        }
    }

    @Override
    public boolean async() {
        return false;
    }

}
