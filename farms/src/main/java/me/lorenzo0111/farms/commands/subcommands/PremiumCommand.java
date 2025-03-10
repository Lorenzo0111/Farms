/*
 * -------------------------------------
 * Copyright Lorenzo0111 2025
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.commands.subcommands;

import me.lorenzo0111.farms.commands.FarmsCommand;
import me.lorenzo0111.farms.commands.SubCommand;
import me.lorenzo0111.farms.premium.PremiumHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class PremiumCommand extends SubCommand {

    public PremiumCommand(FarmsCommand command) {
        super(command);
    }

    @Override
    public String[] getName() {
        return new String[]{"premium"};
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (PremiumHandler.isPremium()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + "&7Running &eFarms&7 licensed to &e" + PremiumHandler.formatUserURL() + "&7."));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + "&7Running the free version of &eFarms&7."));
        }
    }
}
