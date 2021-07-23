package me.lorenzo0111.farms.listeners;

import dev.triumphteam.gui.components.util.ItemNbt;
import lombok.RequiredArgsConstructor;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.api.objects.FarmType;
import me.lorenzo0111.farms.utils.BlockUtils;
import me.lorenzo0111.farms.utils.StandUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

@RequiredArgsConstructor
public class PlaceListener implements Listener {
    private final Farms plugin;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onPlace(BlockPlaceEvent event) {
        if (ItemNbt.getString(event.getItemInHand(), "custom_item") == null)
            return;

        if (event.getBlock().getLocation().clone().subtract(0,1,0).getBlock().getType().equals(Material.BEDROCK)) {
            event.setCancelled(true);
            return;
        }

        event.getBlock().setType(Material.AIR);

        String level = ItemNbt.getString(event.getItemInHand(), "farm_level");
        int levelInt = NumberUtils.toInt(level, 1);

        Location location = event.getBlock().getLocation().subtract(0,1,0);
        Material before = location.getBlock().getType();
        Farm farm = plugin.getDataManager().create(new Farm(
                event.getBlock().getLocation(),
                UUID.randomUUID(),
                event.getPlayer().getUniqueId(),
                levelInt,
                2,
                FarmType.BLOCKS,
                Material.WHEAT,
                before));

        StandUtils.miner(event.getBlock().getLocation().clone().add(0.5,0,0.5), event.getPlayer(), farm.getUuid().toString());

        location.getBlock().setType(Material.GOLD_BLOCK);
        BlockUtils.near(location.getBlock(), 2).forEach(block -> {
            if (block.getType().equals(Material.DIRT) || block.getType().equals(Material.GRASS_BLOCK) || block.getType().equals(Material.AIR)) {
                block.setType(Material.FARMLAND);
            }
        });

        BlockUtils.full(farm, farm.getLocation());

        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("prefix") + plugin.getMessages().getString("setup.created")));
    }
}
