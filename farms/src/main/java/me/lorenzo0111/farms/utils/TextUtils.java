package me.lorenzo0111.farms.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public final class TextUtils {

    public static Component component(FileConfiguration config, String path) {
        return Component.text(text(config,path));
    }

    public static String text(FileConfiguration config, String path) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(path) + "");
    }
}
