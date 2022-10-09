/*
 * -------------------------------------
 * Copyright Lorenzo0111 2022
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.collector;

import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.api.objects.IFarm;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CollectorHandler {
    // Player, Farm
    private static final Map<UUID, UUID> CREATING = new HashMap<>();

    public static void addMember(@NotNull Player player, @NotNull Farm farm) {
        if (contains(player)) removeMember(player);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Farms.getInstance().getMessages().getString("prefix") + Farms.getInstance().getMessages().getString("setup.set-collector")));
        CREATING.put(player.getUniqueId(), farm.getUuid());
    }

    public static void removeMember(@NotNull Player player) {
        CREATING.remove(player.getUniqueId());
    }

    public static boolean contains(@NotNull Player player) {
        return CREATING.containsKey(player.getUniqueId());
    }

    public static boolean canPerform(@NotNull Player player) {
        if (!contains(player)) return false;

        IFarm farm = Farms.getInstance().getDataManager().get(CREATING.get(player.getUniqueId()));
        if (farm == null) {
            Farms.getInstance().getLogger().info(player.getName() + " tried to set collector of an un-existing farm.");
            removeMember(player);
            return false;
        }

        return true;
    }

    public static void complete(@NotNull Player player, @NotNull Block block) {
        if (!canPerform(player)) return;
        IFarm farm = Farms.getInstance().getDataManager().get(CREATING.get(player.getUniqueId()));
        if (farm == null) {
            Farms.getInstance().getLogger().info(player.getName() + " tried to set collector of an un-existing farm.");
            removeMember(player);
            return;
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Farms.getInstance().getMessages().getString("prefix") + Farms.getInstance().getMessages().getString("setup.collector")));
        farm.setCollector(block.getLocation());
        removeMember(player);
    }
}
