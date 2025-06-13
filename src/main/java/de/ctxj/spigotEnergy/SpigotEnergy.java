package de.ctxj.spigotEnergy;

import de.ctxj.spigotEnergy.listeners.BasicGeneratorListener;
import de.ctxj.spigotEnergy.objects.abstr.EnergyItem;
import de.ctxj.spigotEnergy.objects.abstr.EnergyTransferItem;
import de.ctxj.spigotEnergy.objects.abstr.Generator;
import de.ctxj.spigotEnergy.util.EnergyItemManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.atomic.AtomicInteger;

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
        energyItemManager.initialize();

        AtomicInteger configSafeCounter = new AtomicInteger();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            //Autosafe config (every 5 mins)
            if(configSafeCounter.get() >= 300) {
                for(EnergyItem item : energyItemManager.activeItems) {
                    item.cfgUpdateEnergy();
                    if(item instanceof EnergyTransferItem) {
                        ((EnergyTransferItem) item).cfgUpdateDirection();
                    }
                }
                configSafeCounter.set(0);
            } else {
                configSafeCounter.getAndIncrement();
            }

            for(EnergyItem item : energyItemManager.activeItems) {
                if(item instanceof EnergyTransferItem) {
                    ((EnergyTransferItem) item).transferEnergy();
                    if(item instanceof Generator) {
                        ((Generator)item).generate();
                    }
                    Bukkit.getConsoleSender().sendMessage(item.toString() + " | E=" + item.getEnergy());
                }
            }
        }, 20, 40);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for(EnergyItem item : energyItemManager.activeItems) {
            item.cfgUpdateEnergy();
            if(item instanceof EnergyTransferItem) {
                ((EnergyTransferItem) item).cfgUpdateDirection();
            }
        }
    }

    public static SpigotEnergy getInstance() {
        return instance;
    }

    public void setupCommands() {

    }

    public void setupListeners() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new BasicGeneratorListener(), this);
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
