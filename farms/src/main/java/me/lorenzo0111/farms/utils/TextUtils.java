/*
 * -------------------------------------
 * Copyright Lorenzo0111 2024
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.utils;

import com.haroldstudios.hexitextlib.HexResolver;
import me.lorenzo0111.farms.config.UpdatingConfig;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public final class TextUtils {
    public static final Pattern PATTERN = Pattern.compile("&(#[a-fA-F0-9]{6})");

    @Contract("_, _ -> new")
    public static @NotNull Component component(UpdatingConfig config, String path) {
        return Component.text(text(config,path));
    }

    @Contract("_, _ -> new")
    public static @NotNull String text(@NotNull UpdatingConfig config, String path) {
        return HexResolver.parseHexString(config.getString(path) + "", PATTERN);
    }
}
