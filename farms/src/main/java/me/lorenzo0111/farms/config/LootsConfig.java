/*
 * -------------------------------------
 * Copyright Lorenzo0111 2024
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.config;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import me.lorenzo0111.farms.Farms;
import me.lorenzo0111.farms.config.loots.Loot;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class LootsConfig {
    private final File file;
    private FileConfiguration config;
    private Map<EntityType, List<Loot>> loots;

    public LootsConfig(File file) {
        this.file = file;
        this.reload();
    }


    public FileConfiguration getConfig() {
        if (config == null) {
            this.reload();
        }
        return config;
    }

    public @NotNull List<Loot> getLoots(EntityType type) {
        return loots.containsKey(type) ? loots.get(type) : new ArrayList<>();
    }

    public void reload() {
        loots = new HashMap<>();
        config = YamlConfiguration.loadConfiguration(file);
        config.set("data.version", Farms.getInstance().getDescription().getVersion());
        this.save();
        for (String key : config.getKeys(false)) {
            if (key.equalsIgnoreCase("data")) continue;

            try {
                EntityType type = EntityType.valueOf(key);
                List<Loot> loots = new ArrayList<>();

                for (String item : config.getStringList(key)) {
                    int min = Integer.parseInt(item.split("-")[0]);
                    int max = Integer.parseInt(item.split("-")[1].split(":")[0]);
                    String material = item.split(":")[1];

                    loots.add(new Loot(min, max, XMaterial.matchXMaterial(material).orElseThrow(IllegalArgumentException::new)));
                }

                this.loots.put(type,loots);
            } catch (Exception ignored) {}

        }
    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception ignored) {}
    }
}
