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

public class BlocksWorker implements Worker {

    @SuppressWarnings("deprecation")
    @Override
    public void work(DataManager manager, Farm farm) {
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

    @Override
    public boolean async() {
        return false;
    }

}
