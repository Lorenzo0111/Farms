/*
 * -------------------------------------
 * Copyright Lorenzo0111 2021
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.config;

import com.google.common.base.Charsets;
import me.lorenzo0111.farms.Farms;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

public class UpdatingConfig {
    private final File file;
    private final FileConfiguration configuration;

    public UpdatingConfig(@NotNull File file, @Nullable FileConfiguration configuration) {
        this.file = file;
        this.configuration = configuration == null ? YamlConfiguration.loadConfiguration(file) : configuration;

        try(InputStream stream = this.getClass().getClassLoader().getResourceAsStream(file.getName())) {
            if (stream == null) {
                Farms.getInstance().getLogger().info("[UpdatingConfig] Unable to create a stream for " + file.getName() + ".");
                return;
            }

            Reader reader = new InputStreamReader(stream, Charsets.UTF_8);
            this.configuration.setDefaults(YamlConfiguration.loadConfiguration(reader));
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String path) {
        Configuration defaults = configuration.getDefaults();

        Objects.requireNonNull(defaults);

        if (!configuration.contains(path, true) && defaults.contains(path)) {
            configuration.set(path, configuration.getDefaults().get(path));
            this.save();
        }

        return configuration.getString(path);
    }

    private void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }
}
