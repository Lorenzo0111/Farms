/*
 * -------------------------------------
 * Copyright Lorenzo0111 2022
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.commands.subcommands;

import me.lorenzo0111.farms.api.objects.FarmType;
import me.lorenzo0111.farms.commands.FarmsCommand;
import me.lorenzo0111.farms.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class ListCommand extends SubCommand {

    public ListCommand(FarmsCommand command) {
        super(command);
    }

    @Override
    public String[] getName() {
        return new String[]{"list","types"};
    }

    @Override
    public @Nullable String getPermission() {
        return "farms.list";
    }

    @Override
    public void execute(Player player, String[] args) {
        StringBuilder builder = new StringBuilder(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("commands.list") + ""));
        for (FarmType type : FarmType.values()) {
            builder.append(type.name()).append(", ");
        }

        builder.delete(builder.length() - 2, builder.length());
        player.sendMessage(builder.toString());
    }
}
