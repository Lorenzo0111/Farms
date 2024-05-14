/*
 * -------------------------------------
 * Copyright Lorenzo0111 2024
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.commands.subcommands;

import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.commands.FarmsCommand;
import me.lorenzo0111.farms.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class DebugCommand extends SubCommand {

    public DebugCommand(FarmsCommand command) {
        super(command);
    }

    @Override
    public String[] getName() {
        return new String[]{"debug","dump"};
    }

    @Override
    public @Nullable String getPermission() {
        return "farms.admin";
    }

    @Override
    public void execute(Player player, String[] args) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + "&7Preparing to dump all the data..."));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + "&eRunning tasks:"));

        this.getCommand().getPlugin()
                .getDataManager()
                .getFarms()
                .forEach(t -> {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + "&7" + t.getUuid() + " (&e" + ((Farm) t).getTask() + "&7)"));
                });
    }
}
