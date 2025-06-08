package de.ctxj.spigotEnergy;

import de.ctxj.spigotEnergy.objects.abstr.Generator;
import de.ctxj.spigotEnergy.util.EnergyItemManager;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class SpigotEnergy extends JavaPlugin {

    private static SpigotEnergy instance;
    private static EnergyItemManager energyItemManager;


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        setupConfig();
        setupCommands();
        setupListeners();
        energyItemManager = new EnergyItemManager();

        //Player player = Bukkit.getServer().getPlayer("ctxj");
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

    public static EnergyItemManager getEnergyItemManager() {
        return energyItemManager;
    }

    public String getItemFilePath() {
        return getConfig().getString("itemFile");
    }
}
