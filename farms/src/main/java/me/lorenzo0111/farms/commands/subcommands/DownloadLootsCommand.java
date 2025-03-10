/*
 * -------------------------------------
 * Copyright Lorenzo0111 2025
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.commands.subcommands;

import me.lorenzo0111.farms.commands.FarmsCommand;
import me.lorenzo0111.farms.commands.SubCommand;
import me.lorenzo0111.farms.web.FileDownloader;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;

public class DownloadLootsCommand extends SubCommand {

    public DownloadLootsCommand(FarmsCommand command) {
        super(command);
    }

    @Override
    public String[] getName() {
        return new String[]{"dl", "downloadLoots"};
    }

    @Override
    public @Nullable String getPermission() {
        return "farms.admin";
    }

    @Override
    public void execute(Player player, String[] args) {
        try {
            Instant now = Instant.now();
            FileDownloader downloader = new FileDownloader("https://raw.githubusercontent.com/Lorenzo0111/PluginsDatabase/master/Farms/loots.yml");
            downloader.appendLogger(this.getCommand().getPlugin().getLogger());
            if (downloader.download(this.getCommand().getPlugin().getDataFolder(), "loots.yml", true)) {
                this.getCommand().getPlugin().getLoots().reload();
                Instant end = Instant.now();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getMessages().getString("prefix") + this.getCommand().getPlugin().getMessages().getString("commands.download") + "").replace("%ms%",String.valueOf(Duration.between(now,end).toMillis())));
            }

        } catch (Exception ignored) {}
    }
}
