/*
 * -------------------------------------
 * Copyright Lorenzo0111 2021
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.commands;

import lombok.Getter;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.commands.subcommands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class FarmsCommand implements CommandExecutor, TabExecutor {
    private final Farms plugin;
    private final List<SubCommand> subCommands = new ArrayList<>();

    public FarmsCommand(Farms plugin) {
        this.plugin = plugin;

        subCommands.add(new CreateCommand(this));
        subCommands.add(new GUICommand(this));
        subCommands.add(new HelpCommand(this));
        subCommands.add(new ReloadCommand(this));
        subCommands.add(new RemoveAllCommand(this));
        subCommands.add(new DownloadLootsCommand(this));
        subCommands.add(new GiveCommand(this));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed from a player.");
            return true;
        }

        if (args.length > 0) {
            for (SubCommand subcommand : subCommands) {
                if (Arrays.asList(subcommand.getName()).contains(args[0].toLowerCase())) {
                    if (subcommand.getPermission() != null && !sender.hasPermission(subcommand.getPermission())) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("prefix") + plugin.getMessages().getString("no-permission")));
                        return true;
                    }

                    subcommand.execute((Player) sender, args);
                    return true;
                }
            }
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("commands.no-args", "")
                    .replace("%author%", "Lorenzo0111")
                    .replace("%version%", plugin.getDescription().getVersion())));
            return true;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("prefix") + plugin.getMessages().getString("commands.not-found")));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
        List<String> list = new ArrayList<>();

        if (args.length == 0 || args.length == 1) {
            for (SubCommand subCommand : subCommands){
                for (String name : subCommand.getName()) {
                    if (name.toLowerCase().startsWith(args[0].toLowerCase())) {
                        if (subCommand.getPermission() == null || sender.hasPermission(subCommand.getPermission()))
                            list.add(name);
                    }
                }
            }
        }

        return list;
    }
}
