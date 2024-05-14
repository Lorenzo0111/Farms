/*
 * -------------------------------------
 * Copyright Lorenzo0111 2024
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
import me.lorenzo0111.farms.commands.subcommands.CreateCommand;
import me.lorenzo0111.farms.config.LootsConfig;
import me.lorenzo0111.farms.config.UpdatingConfig;
import me.lorenzo0111.farms.data.DataManager;
import me.lorenzo0111.farms.hooks.VaultHook;
import me.lorenzo0111.farms.hooks.WorldGuardHook;
import me.lorenzo0111.farms.listeners.*;
import me.lorenzo0111.farms.premium.PremiumHandler;
import me.lorenzo0111.farms.web.FileDownloader;
import me.lorenzo0111.farms.web.UpdateChecker;
import me.lorenzo0111.farms.tasks.FarmsTask;
import me.lorenzo0111.farms.tasks.QueueTask;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@Getter
public final class Farms extends JavaPlugin {
    private File dataFile;
    private FileConfiguration data;
    private DataManager dataManager;

    private LootsConfig loots;

    private File configFile;
    private UpdatingConfig config;

    private File messagesFile;
    private UpdatingConfig messages;

    private File guiFile;
    private FileConfiguration guiConfig;

    @Getter private static Farms instance;
    @Getter private UpdateChecker updater;

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
        MinecraftVersion.disableUpdateCheck();
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

            this.configFile = new File(this.getDataFolder(), "config.yml");
            this.extract(configFile);
            this.reloadConfig();
            this.config = new UpdatingConfig(configFile);

            this.messagesFile = new File(this.getDataFolder(), "messages.yml");
            this.extract(messagesFile);

            this.guiFile = new File(this.getDataFolder(), "gui.yml");
            this.extract(guiFile);

            dataManager.reload();
            dataManager.init();
            this.reload();

        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        this.getLogger().info("Loaded " + dataManager.getFarms().size() + " farms.");

        this.getLogger().info("Registering listeners...");

        Bukkit.getPluginManager().registerEvents(new BlockListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BreakListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CollectorListener(this), this);
        Bukkit.getPluginManager().registerEvents(new DamageListener(this), this);
        Bukkit.getPluginManager().registerEvents(new InteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlaceListener(this), this);

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
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new FarmsTask(this), 0, getConfig().getInt("tasks.grow", 5) * 20L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new QueueTask(this), 0, getConfig().getInt("tasks.collect", 10) * 20L);

        this.getLogger().info("Loading API...");
        Bukkit.getServicesManager().register(IFarmsAPI.class,new FarmsAPI(this),this, ServicePriority.Normal);

        new Metrics(this, 12310)
                .addCustomChart(new SingleLineChart("farms", () -> dataManager.getFarms().size()));

        this.updater = new UpdateChecker(this,PremiumHandler.isPremium() ? 94931 : 98747);

        try {
            boolean overWrite = false;
            File file = new File(this.getDataFolder(), "loots.yml");
            if (file.exists()) {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                if (!config.getString("data.version", this.getDescription().getVersion()).equals(this.getDescription().getVersion())) {
                    overWrite = true;
                    this.getLogger().info("Found old loots.yml file. Downloading it again..");
                }
            }

            Instant now = Instant.now();
            FileDownloader downloader = new FileDownloader("https://raw.githubusercontent.com/Lorenzo0111/PluginsDatabase/master/Farms/loots.yml");
            downloader.appendLogger(this.getLogger());
            if (downloader.download(this.getDataFolder(), "loots.yml", overWrite)) {
                Instant end = Instant.now();
                this.getLogger().info("Downloaded loots.yml in " + Duration.between(now, end).getSeconds() + " seconds.");
            }
            loots = new LootsConfig(new File(this.getDataFolder(), "loots.yml"));
        } catch (MalformedURLException ignored) {}

        if (PremiumHandler.isPremium()) {
            this.getLogger().info("Premium detected! Thanks for purchasing the plugin!");
        } else {
            this.getLogger().info("If you want to support my work you can buy the premium version on: https://www.spigotmc.org/resources/94931/");
        }

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

        try {
            messages = new UpdatingConfig(messagesFile);
            config = new UpdatingConfig(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        guiConfig = YamlConfiguration.loadConfiguration(guiFile);
        CreateCommand.resetItem();
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

    @NotNull
    @Override
    public UpdatingConfig getConfig() {
        return config;
    }

    private void extract(@NotNull File file) {
        if (!file.exists()) {
            try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(file.getName())) {
                Objects.requireNonNull(in);

                Files.copy(in, file.toPath());
            } catch (IOException e) {
                this.getLogger().severe("Unable to create " + file.getName() + " file. Disabling..");
                e.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(this);
            }
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
