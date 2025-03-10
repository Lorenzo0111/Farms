/*
 * -------------------------------------
 * Copyright Lorenzo0111 2025
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.commands.subcommands;

import me.lorenzo0111.farms.commands.FarmsCommand;
import me.lorenzo0111.farms.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class ReloadCommand extends SubCommand {

    public ReloadCommand(FarmsCommand command) {
        super(command);
    }

    @Override
    public String[] getName() {
        return new String[]{"reload"};
    }

    @Override
    public @Nullable String getPermission() {
        return "farms.admin";
    }

    @Override
    public void execute(Player player, String[] args) {
        long ms = System.currentTimeMillis();
        this.getCommand().getPlugin().reload();
        ms = System.currentTimeMillis() - ms;

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + this.getCommand().getPlugin().getMessages().getString("commands.reload") + "").replace("%ms%",String.valueOf(ms)));
    }
}
