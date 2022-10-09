/*
 * -------------------------------------
 * Copyright Lorenzo0111 2022
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.config;

import com.google.common.base.Charsets;
import me.lorenzo0111.farms.Farms;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class UpdatingConfig extends YamlConfiguration {
    private final File file;

    public UpdatingConfig(@NotNull File file) throws IOException, InvalidConfigurationException {
        super();
        this.load(file);
        this.file = file;

        final InputStream defConfigStream = Farms.getInstance().getResource(file.getName());
        if (defConfigStream == null) {
            return;
        }

        InputStreamReader inputStreamReader = new InputStreamReader(defConfigStream, Charsets.UTF_8);

        this.setDefaults(loadConfiguration(inputStreamReader));
        defConfigStream.close();
        inputStreamReader.close();
    }

    @Override
    public String getString(@NotNull String path) {
        return (String) get(path);
    }

    @Override
    public int getInt(@NotNull String path) {
        return (int) this.get(path);
    }

    @Override
    public double getDouble(@NotNull String path) {
        return (double) this.get(path);
    }

    @Override
    public @NotNull Object get(@NotNull String path) {
        Configuration defaults = this.getDefaults();

        Objects.requireNonNull(defaults);

        if (!this.contains(path, true) && defaults.contains(path)) {
            this.set(path, this.getDefaults().get(path));
            this.save();
        }

        return super.get(path, "");
    }

    @Override
    public boolean contains(@NotNull String path) {
        return super.contains(path);
    }

    @Override
    public boolean contains(@NotNull String path, boolean ignoreDefault) {
        return super.contains(path, ignoreDefault);
    }

    public void save() {
        try {
            this.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
