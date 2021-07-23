package me.lorenzo0111.farms.tasks;

import lombok.RequiredArgsConstructor;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.utils.BlockUtils;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;

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

                if (block.getBlockData() instanceof Ageable) {
                    Ageable data = (Ageable) block.getBlockData();
                    if (data.getAge() != data.getMaximumAge()) {
                        data.setAge(data.getAge() + 1);
                        block.setBlockData(data);
                        executed++;
                    }
                }
            }
        }

    }

}
