/*
 * -------------------------------------
 * Copyright Lorenzo0111 2025
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.lorenzo0111.farms.Farms;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WorldGuardHook {
    private static boolean hook = false;
    private static WorldGuardPlugin plugin;
    private static WorldGuard api;

    public static boolean init(@NotNull Farms plugin) {
        if (!plugin.getConfig().getBoolean("worldguard.enabled")) {
            return false;
        }

        if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            hook = true;
            try {
                api = WorldGuard.getInstance();
                WorldGuardHook.plugin = WorldGuardPlugin.getPlugin(WorldGuardPlugin.class);
            } catch (NoClassDefFoundError | NoSuchMethodError ignored) {
               hook = false;
               return false;
            }

            return true;
        }

        return false;
    }

    public static boolean canBuild(Player player, Location location) {
        if (!hook) return true;

        LocalPlayer local = plugin.wrapPlayer(player);

        if (api.getPlatform().getSessionManager().hasBypass(local,local.getWorld())) return true;

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(location);
        RegionQuery query = container.createQuery();

        return query.testState(loc, local, Flags.BUILD);
    }
}

