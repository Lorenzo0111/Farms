/*
 * -------------------------------------
 * Copyright Lorenzo0111 2021
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms;

import de.tr7zw.changeme.nbtapi.utils.MinecraftVersion;
import lombok.Getter;
import me.lorenzo0111.farms.api.FarmsAPI;
import me.lorenzo0111.farms.api.IFarmsAPI;
import me.lorenzo0111.farms.api.objects.Farm;
import me.lorenzo0111.farms.commands.FarmsCommand;
import me.lorenzo0111.farms.config.UpdatingConfig;
import me.lorenzo0111.farms.data.DataManager;
import me.lorenzo0111.farms.hooks.VaultHook;
import me.lorenzo0111.farms.hooks.WorldGuardHook;
import me.lorenzo0111.farms.tasks.FarmsTask;
import me.lorenzo0111.farms.tasks.QueueTask;
import me.lorenzo0111.farms.tasks.SaveTask;
import me.lorenzo0111.farms.utils.ReflectionHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Objects;

@Getter
public final class Farms extends JavaPlugin {
    private File dataFile;
    private FileConfiguration data;
    private DataManager dataManager;

    private File messagesFile;
    private UpdatingConfig messages;

    private File guiFile;
    private FileConfiguration guiConfig;

    @Getter private static Farms instance;

    @Override
    public void onLoad() {
        MinecraftVersion.replaceLogger(this.getLogger());
    }

    @Override
    public void onEnable() {
        instance = this;
        long ms = System.currentTimeMillis();

        ConfigurationSerialization.registerClass(Farm.class);
        this.saveDefaultConfig();

        this.getLogger().info("Loading NBTAPI..");
        MinecraftVersion.getVersion();

        this.getLogger().info("Loading existing farms..");
        this.dataFile = new File(this.getDataFolder(), "data.yml");
        this.getDataFolder().mkdirs();

        try {

            if (!dataFile.exists() && !dataFile.createNewFile()) {
                this.getLogger().severe("Unable to create data.yml file. Disabling..");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }

            data = YamlConfiguration.loadConfiguration(dataFile);
            dataManager = new DataManager(this);

            this.messagesFile = new File(this.getDataFolder(), "messages.yml");

            if (!messagesFile.exists()) {
                try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("messages.yml")) {
                    Objects.requireNonNull(in);

                    Files.copy(in, messagesFile.toPath());
                } catch (IOException e) {
                    this.getLogger().severe("Unable to create messages.yml file. Disabling..");
                    e.printStackTrace();
                    Bukkit.getPluginManager().disablePlugin(this);
                    return;
                }
            }

            this.guiFile = new File(this.getDataFolder(), "gui.yml");

            if (!guiFile.exists()) {
                try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("gui.yml")) {
                    Objects.requireNonNull(in);

                    Files.copy(in, guiFile.toPath());
                } catch (IOException e) {
                    this.getLogger().severe("Unable to create gui.yml file. Disabling..");
                    e.printStackTrace();
                    Bukkit.getPluginManager().disablePlugin(this);
                    return;
                }
            }

            dataManager.reload();
            dataManager.init();
            this.reload();

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.getLogger().info("Loaded " + dataManager.getFarms().size() + " farms.");

        this.getLogger().info("Registering listeners...");

        ReflectionHandler.with(this.getClass().getPackage().getName()+".listeners", Listener.class, Farms.class)
            .build(this)
            .forEach((listener) -> Bukkit.getPluginManager().registerEvents(listener,this));

        this.getLogger().info("Looking for Vault..");
        if (VaultHook.init(this)) {
            this.getLogger().info("Vault hooked! Using Vault economy..");
        }

        this.getLogger().info("Looking for WorldGuard..");
        if (WorldGuardHook.init(this)) {
            this.getLogger().info("WorldGuard hooked! Using WorldGuard api..");
        }

        this.getLogger().info("Loading commands..");
        FarmsCommand command = new FarmsCommand(this);
        PluginCommand farmsCmd = this.getCommand("farms");

        Objects.requireNonNull(farmsCmd);

        farmsCmd.setExecutor(command);
        farmsCmd.setTabCompleter(command);

        this.getLogger().info("Scheduling tasks...");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new SaveTask(this), 0, 60 * 20L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new FarmsTask(this), 0, getConfig().getInt("tasks.grow", 5) * 20L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new QueueTask(this), 0, getConfig().getInt("tasks.collect", 10) * 20L);

        this.getLogger().info("Loading API...");
        Bukkit.getServicesManager().register(IFarmsAPI.class,new FarmsAPI(this),this, ServicePriority.Normal);

        ms = System.currentTimeMillis() - ms;
        this.getLogger().info(getName() + " v" + getDescription().getVersion() + " enabled in " + ms + "ms.");
    }

    @Override
    public void onDisable() {
        if (dataManager != null)
            dataManager.save(true);
        Bukkit.getScheduler().cancelTasks(this);
    }

    public void reload() {
        dataManager.save(false);
        this.reloadData();

        messages = new UpdatingConfig(messagesFile,null);
        guiConfig = YamlConfiguration.loadConfiguration(guiFile);

        this.reloadConfig();
    }

    public void reloadData() {
        try {
            data.save(dataFile);
            data = YamlConfiguration.loadConfiguration(dataFile);
            dataManager.reload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void debug(Object... messages) {
        if(!this.getConfig().getBoolean("debug"))
            return;

        for (Object message : messages) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&' , this.getMessages().getString("prefix") + "&8[&eDebug&8] &r" + message));
        }
    }
}
