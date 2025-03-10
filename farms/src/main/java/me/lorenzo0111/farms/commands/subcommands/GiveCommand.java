/*
 * -------------------------------------
 * Copyright Lorenzo0111 2025
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.commands.subcommands;

import me.lorenzo0111.farms.api.objects.FarmType;
import me.lorenzo0111.farms.commands.FarmsCommand;
import me.lorenzo0111.farms.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class GiveCommand extends SubCommand {

    public GiveCommand(FarmsCommand command) {
        super(command);
    }

    @Override
    public String[] getName() {
        return new String[]{"give"};
    }

    @Override
    public @Nullable String getPermission() {
        return "farms.give";
    }

    @Override
    public void execute(Player player, String @NotNull [] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + this.getCommand().getPlugin().getMessages().getString("commands.no-player")));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + this.getCommand().getPlugin().getMessages().getString("not-found")));
            return;
        }

        FarmType type = FarmType.BLOCKS;
        if (args.length == 3) {
            try {
                type = FarmType.valueOf(args[2].toUpperCase());
            } catch (IllegalArgumentException ignored) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + this.getCommand().getPlugin().getMessages().getString("commands.no-type")));
                return;
            }
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + Objects.requireNonNull(this.getCommand()
                        .getPlugin()
                        .getMessages()
                        .getString("commands.give"))
                .replace("%player%", target.getName())
                .replace("%type%", type.name().toLowerCase())));

        ItemStack item = CreateCommand.getItem(type);
        target.getInventory().addItem(item);
    }
}
