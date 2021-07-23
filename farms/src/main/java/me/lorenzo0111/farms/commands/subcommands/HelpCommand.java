package me.lorenzo0111.farms.commands.subcommands;

import me.lorenzo0111.farms.commands.FarmsCommand;
import me.lorenzo0111.farms.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class HelpCommand extends SubCommand {

    public HelpCommand(FarmsCommand plugin) {
        super(plugin);
    }

    @Override
    public String[] getName() {
        return new String[]{"help"};
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }

    @Override
    public void execute(Player player, String[] args) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("commands.help") + ""));
    }
}
