/*
 * -------------------------------------
 * Copyright Lorenzo0111 2021
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.tasks;

import com.cryptomorin.xseries.XBlock;
import lombok.RequiredArgsConstructor;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.utils.BlockUtils;
import org.bukkit.block.Block;

import java.util.List;

@RequiredArgsConstructor
public class FarmsTask implements Runnable {
    private final Farms plugin;

    @Override
    public void run() {
        List<Farm> farms = plugin.getDataManager().getFarms();

        for (Farm farm : farms) {
            int executed = 0;

            for (Block block : BlockUtils.near(farm.getLocation().clone().getBlock(), farm.getRadius())) {

                if (executed >= 5)
                    continue;

                int age = XBlock.getAge(block);
                if (age < 7) {
                    XBlock.setAge(block, age+1);
                    executed++;
                }
            }
        }

    }

}
