/*
 * -------------------------------------
 * Copyright Lorenzo0111 2024
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.listeners;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.RequiredArgsConstructor;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.api.objects.FarmType;
import me.lorenzo0111.farms.commands.subcommands.CreateCommand;
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
        if (!event.getItemInHand().getType().equals(CreateCommand.getItem().getItem().getType())) return;

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
        FarmType type = FarmType.valueOf(compound.getString("farm_type"));

        Location location = event.getBlock().getLocation().subtract(0,1,0);
        Material before = location.getBlock().getType();

        int radius = plugin.getConfig().getInt("default-radius");

        Farm farm = plugin.getDataManager().create(new Farm(
                event.getBlock().getLocation(),
                UUID.randomUUID(),
                event.getPlayer().getUniqueId(),
                level,
                radius,
                type,
                Material.WHEAT,
                before));

        StandUtils.miner(event.getBlock().getLocation().clone().add(0.5,0,0.5), event.getPlayer(), farm.getUuid().toString());

        location.getBlock().setType(Material.GOLD_BLOCK);
        if (farm.getType().place()) {
            BlockUtils.near(location.getBlock(), farm.getRadius()).forEach(block -> {
                if (block.getType().equals(Material.DIRT) || block.getType().equals(XMaterial.GRASS_BLOCK.parseMaterial()) || block.getType().equals(Material.AIR)) {
                    block.setType(XMaterial.FARMLAND.parseMaterial());
                }
            });

            BlockUtils.full(farm, farm.getLocation());
        }

        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("prefix") + plugin.getMessages().getString("setup.created")));
    }
}
