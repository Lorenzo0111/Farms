/*
 * -------------------------------------
 * Copyright Lorenzo0111 2025
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.web;

import java.io.IOException;
import java.net.URL;

import me.lorenzo0111.farms.Farms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Spigot Update Checker
 */
@SuppressWarnings("unused")
public class UpdateChecker {
    private boolean fetched = false;
    private boolean updateAvailable;
    private String newVersion;

    private final Farms plugin;
    private final String api;
    /**
     * @param plugin Plugin
     * @param resourceId ResourceID
     */
    public UpdateChecker(Farms plugin, int resourceId) {
        this.plugin = plugin;

        this.api = "https://api.spigotmc.org/legacy/update.php?resource=" + resourceId;

        this.fetch();
    }

    /**
     * Fetch updates from hangar api
     */
    private @NotNull CompletableFuture<Void> fetch() {
        Executor asyncExecutor = (cmd) -> Bukkit.getScheduler().runTaskAsynchronously(plugin, cmd);

        return CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL(api);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                newVersion = content.toString();
                this.fetched = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, asyncExecutor);
    }

    /**
     * @param entity Entity to send the update message
     */
    public void sendUpdateCheck(CommandSender entity) {
        if (!fetched) {
            this.fetch().thenAccept((unused) -> sendUpdateCheck(entity));
            return;
        }

        if (updateAvailable) {
            entity.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessages().getString("prefix") + "A new update of &e&nFarms&7 is available. New version: &e&n" + newVersion));
        }
    }

}