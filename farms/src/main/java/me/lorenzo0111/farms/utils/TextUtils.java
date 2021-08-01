package me.lorenzo0111.farms.utils;

import me.lorenzo0111.farms.config.UpdatingConfig;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class TextUtils {

    @Contract("_, _ -> new")
    public static @NotNull Component component(UpdatingConfig config, String path) {
        return Component.text(text(config,path));
    }

    @Contract("_, _ -> new")
    public static @NotNull String text(@NotNull UpdatingConfig config, String path) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(path) + "");
    }
}
