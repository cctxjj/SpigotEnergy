package de.ctxj.spigotEnergy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpigotEnergy extends JavaPlugin {

    private static SpigotEnergy instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        setupConfig();
        setupCommands();
        setupListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static SpigotEnergy getInstance() {
        return instance;
    }

    public void setupCommands() {

    }

    public void setupListeners() {
        PluginManager manager = Bukkit.getPluginManager();

    }

    public void setupConfig() {
        getConfig().options().copyDefaults(true);
        getConfig().addDefault("itemFile", "plugins/SpigotEnergy/items.yml");
        saveConfig();
    }

    public String getItemFilePath() {
        return getConfig().getString("itemFile");
    }
}
