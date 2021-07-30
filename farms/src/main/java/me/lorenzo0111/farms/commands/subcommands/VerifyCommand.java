package me.lorenzo0111.farms.commands.subcommands;

import me.lorenzo0111.farms.commands.FarmsCommand;
import me.lorenzo0111.farms.commands.SubCommand;
import me.lorenzo0111.farms.premium.PremiumHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VerifyCommand extends SubCommand {

    public VerifyCommand(FarmsCommand command) {
        super(command);
    }

    @Override
    public String[] getName() {
        return new String[]{"verify","premium"};
    }

    @Override
    public @Nullable String getPermission() {
        return "farms.premium.verify";
    }

    @Override
    public void execute(@NotNull Player player, String[] args) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + "Hey &e&n" + player.getName() + "&7, thanks for buying " + this.getCommand().getPlugin().getName() + " v" + this.getCommand().getPlugin().getDescription().getVersion() + "."));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + "&eBuyer information:"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + "  &7Buyer ID: &e" + PremiumHandler.getUserID()));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + "  &7Resource ID: &e" + PremiumHandler.getResourceID()));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + "  &7Download ID: &e" + PremiumHandler.getDownloadID()));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + "  &7Verified: &e" + PremiumHandler.isPremium()));
    }
}
