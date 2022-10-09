/*
 * -------------------------------------
 * Copyright Lorenzo0111 2022
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.hooks;

import me.lorenzo0111.farms.Farms;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {
    private static boolean hook = false;
    private static Economy economy;

    public static boolean init(Farms plugin) {
        if (!plugin.getConfig().getBoolean("vault.enabled")) {
            return false;
        }

        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {

            RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                economy = rsp.getProvider();
                hook = true;
                return true;
            }

        }

        return false;
    }

    public static boolean hasMoney(OfflinePlayer player, double amount) {
        if (!hook) return true;

        return economy.has(player,amount);
    }

    public static boolean withdraw(OfflinePlayer player, double amount) {
        if (!hook) return true;

        if (!hasMoney(player,amount)) return false;

        EconomyResponse economyResponse = economy.withdrawPlayer(player, amount);
        return true;
    }
}
