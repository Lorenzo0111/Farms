package me.lorenzo0111.farms.listeners;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.RequiredArgsConstructor;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.api.objects.FarmType;
import me.lorenzo0111.farms.utils.BlockUtils;
import me.lorenzo0111.farms.utils.StandUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor
public class PlaceListener implements Listener {
    private final Farms plugin;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onPlace(@NotNull BlockPlaceEvent event) {
        NBTContainer item = NBTItem.convertItemtoNBT(event.getItemInHand());

        NBTCompound compound = item.getCompound("tag");
        if (compound == null) return;

        if (!"farms".equals(compound.getString("custom_item"))) {
            return;
        }

        if (event.getBlock().getLocation().clone().subtract(0,1,0).getBlock().getType().equals(Material.BEDROCK)) {
            event.setCancelled(true);
            return;
        }

        event.getBlock().setType(Material.AIR);

        int level = compound.getInteger("farm_level");

        Location location = event.getBlock().getLocation().subtract(0,1,0);
        Material before = location.getBlock().getType();
        Farm farm = plugin.getDataManager().create(new Farm(
                event.getBlock().getLocation(),
                UUID.randomUUID(),
                event.getPlayer().getUniqueId(),
                level,
                2,
                FarmType.BLOCKS,
                Material.WHEAT,
                before));

        StandUtils.miner(event.getBlock().getLocation().clone().add(0.5,0,0.5), event.getPlayer(), farm.getUuid().toString());

        location.getBlock().setType(Material.GOLD_BLOCK);
        BlockUtils.near(location.getBlock(), 2).forEach(block -> {
            if (block.getType().equals(Material.DIRT) || block.getType().equals(XMaterial.GRASS_BLOCK.parseMaterial()) || block.getType().equals(Material.AIR)) {
                block.setType(XMaterial.FARMLAND.parseMaterial());
            }
        });

        BlockUtils.full(farm, farm.getLocation());

        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("prefix") + plugin.getMessages().getString("setup.created")));
    }
}
